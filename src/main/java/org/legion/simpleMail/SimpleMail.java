package org.legion.simpleMail;

import org.bukkit.plugin.java.JavaPlugin;
import org.legion.simpleMail.Mails.Mails;
import org.legion.simpleMail.commands.MailCommand;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.legion.simpleMail.Utils.color;

public final class SimpleMail extends JavaPlugin {

    private static SimpleMail instance;
    private Connection connection;
    private Mails mails;

    public static SimpleMail getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        setupDatabase();

        this.mails = new Mails(connection);
        saveDefaultConfig();

        getCommand("mail").setExecutor(new MailCommand());

        getLogger().info(color("&aSimpleMail loaded successfully!"));
    }

    @Override
    public void onDisable() {
        closeDatabase();
        getLogger().info(color("&cSimpleMail stopped successfully!"));
    }

    private void setupDatabase() {
        try {
            File dbFile = new File(getDataFolder(), "mail.db");
            if (!dbFile.getParentFile().exists()) dbFile.getParentFile().mkdirs();

            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Mails getMails() {
        return mails;
    }

    private void closeDatabase() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
