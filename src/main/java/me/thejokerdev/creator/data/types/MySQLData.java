package me.thejokerdev.creator.data.types;

import me.thejokerdev.creator.Main;
import me.thejokerdev.creator.data.Data;
import me.thejokerdev.creator.data.DataType;
import me.thejokerdev.creator.player.CPlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.*;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MySQLData extends Data {

    private String host;
    private int port;
    private String database;
    private String user;
    private String password;
    private Connection connection;
    private boolean running = false;

    private final String table_users;
    private final String table_creators;

    public MySQLData(Main plugin) {
        super(plugin);
        table_users = plugin.getConfig().getString("settings.data.mysql.tables.user");
        table_creators = plugin.getConfig().getString("settings.data.mysql.tables.creators");

    }

    @Override
    public DataType getType() {
        return DataType.MYSQL;
    }

    @Override
    public void syncData(CPlayer var) {
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement("UPDATE "+table_users+" SET uuid=?, date=?, interacted=? WHERE uuid=?");

            st.setString(1, var.getUniqueId().toString());
            st.setString(2, var.getDateOfInteraction().toString());
            st.setBoolean(3, var.isInteracted());
            st.setString(4, var.getUniqueId().toString());
            st.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            close(st);
        }
    }

    @Override
    public void getData(CPlayer var) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = connection.prepareStatement("SELECT * FROM "+table_users+" WHERE uuid=?");
            st.setString(1, var.getUniqueId().toString());
            rs = st.executeQuery();
            if (rs.next()){
                String date = rs.getString("lang");
                var.setDateOfInteraction(date.isEmpty() ? null : new Date(date));
                var.setInteracted(rs.getBoolean("interacted"));
            } else{
                rs.close();
                st.close();

                st = connection.prepareStatement("INSERT INTO "+table_users+" (uuid) VALUES (?)");
                st.setString(1, var.getUniqueId().toString());
                st.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(st);
        }
    }

    public static void close(AutoCloseable var1) {
        if (var1 != null) {
            try {
                var1.close();
            } catch (Exception ignored) {
            }
        }

    }


    @Override
    public void reload() {

    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void setup() {
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("settings.data.mysql");
        if(section == null) {
            System.out.println("[MySQL] No parameters to try connection.");
            return;
        }
        this.host = section.getString("host").split(":")[0];
        this.port = 3306;
        if (section.getString("host").contains(":")) {
            try {
                port = Integer.parseInt(section.getString("host").split(":")[1]);
            } catch (NumberFormatException e) {
                plugin.error(e);
            }
        }
        this.database = section.getString("database");
        this.user = section.getString("user");
        this.password = section.getString("password");
        this.connect();
        String query = "CREATE TABLE IF NOT EXISTS "+table_users+" (" +
                "UUID varchar(64) NOT NULL, " +
                "interacted BOOLEAN DEFAULT TRUE," +
                "date varchar(32) DEFAULT ''"+
                ");";
        this.update(query);
        running = true;
    }

    public boolean isConnected() {
        return connection != null;
    }

    public Connection getConnection() {
        return connection;
    }

    public void connect() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?autoReconnect=true", this.user, this.password);
            plugin.debug("[MySQL] Connected.");
        }
        catch (SQLException ex) {
            plugin.error(ex);
            plugin.debug("[MySQL] Can't connect.");
        }
    }

    public void disconnect() {
        try {
            this.connection.close();
            plugin.debug("[MySQL] Successfully disconnected.");
        }
        catch (SQLException ex) {
            plugin.error(ex);
            System.out.println("[MySQL] Can't disconnect from server.");
        }
    }

    public void update(final String qry) {
        if (this.isConnected()) {
            new FutureTask<>(new Runnable() {
                PreparedStatement ps;

                @Override
                public void run() {
                    try {
                        (this.ps = connection.prepareStatement(qry)).executeUpdate();
                        this.ps.close();
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }, 1).run();
        }
        else {
            this.connect();
        }
    }

    public ResultSet getResult(final String qry) {
        if (this.isConnected()) {
            try {
                final FutureTask<ResultSet> task = new FutureTask<>(new Callable<ResultSet>() {
                    PreparedStatement ps;

                    @Override
                    public ResultSet call() throws Exception {
                        this.ps = connection.prepareStatement(qry);
                        return this.ps.executeQuery();
                    }
                });
                task.run();
                return task.get();
            }
            catch (InterruptedException | ExecutionException ex2) {
                ex2.printStackTrace();
                return null;
            }
        }
        this.connect();
        return null;
    }
}
