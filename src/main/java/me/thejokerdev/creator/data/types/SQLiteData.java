package me.thejokerdev.creator.data.types;

import me.thejokerdev.creator.Main;
import me.thejokerdev.creator.data.Data;
import me.thejokerdev.creator.data.DataType;
import me.thejokerdev.creator.player.CPlayer;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class SQLiteData extends Data {

    private Connection connection;
    private boolean running = false;

    private final String table_users;
    private final String table_creators;

    public SQLiteData(Main plugin) {
        super(plugin);
        table_users = plugin.getConfig().getString("settings.data.mysql.tables.user");
        table_creators = plugin.getConfig().getString("settings.data.mysql.tables.creators");
    }

    @Override
    public DataType getType() {
        return DataType.SQLITE;
    }

    @Override
    public void syncData(CPlayer var) {
        ResultSet rs = getResult("SELECT * FROM "+table_users+" WHERE uuid = '"+ var.getUniqueId() +"';");

        if(rs != null) {
            update("UPDATE "+table_users+" SET interacted='"+var.isInteracted()+"', date='"+var.getDateOfInteraction()+"' WHERE uuid='"+var.getUniqueId()+"';");
        } else {
            update("INSERT INTO "+table_users+" (UUID, firstJoin, lang) VALUES('"+var.getUniqueId()+"', '"+var.isInteracted()+"', '"+var.getDateOfInteraction()+"');");
        }
    }

    @Override
    public void getData(CPlayer var) {
        ResultSet rs = getResult("SELECT * FROM "+table_users+" WHERE uuid = '"+ var.getUniqueId() +"';");

        if(rs == null) {
            var.setInteracted(false);
            var.setDateOfInteraction(null);
            return;
        }

        try {
            var.setDateOfInteraction(new Date(rs.getString("date")));
            var.setInteracted(rs.getBoolean("interacted"));
        } catch (SQLException e) {
            plugin.error(e);
            var.setInteracted(false);
            var.setDateOfInteraction(null);
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
        this.connect();
        String query = "CREATE TABLE IF NOT EXISTS "+table_users+" (" +
                "UUID varchar(64) NOT NULL, " +
                "interacted boolean NOT NULL DEFAULT true, " +
                "date varchar(100) DEFAULT ''" +
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
        File dataFolder = new File(plugin.getDataFolder(), plugin.getConfig().getString("settings.data.sqlite.file"));
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
        } catch (SQLException|ClassNotFoundException ex) {
            plugin.error(ex);
            plugin.debug("[SQLite] Can't connect.");
        }
    }

    public void disconnect() {
        try {
            this.connection.close();
            plugin.debug("[MySQL] Successfully disconnected.");
        }
        catch (SQLException ex) {
            plugin.error(ex);
            plugin.debug("[MySQL] Can't disconnect.");
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
