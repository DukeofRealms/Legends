/*******************************************************************************
 * This file is part of Champions.
 *
 *     Champions is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Champions is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Champions.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.mcthepond.champs.library.database;

import com.mcthepond.champs.library.BasicCategory;
import com.mcthepond.champs.library.cclass.CClassHandler;
import com.mcthepond.champs.library.configuration.BaseConfiguration;
import com.mcthepond.champs.library.cclass.CClass;
import com.mcthepond.champs.library.configuration.file.YamlConfiguration;
import com.mcthepond.champs.library.cplayer.CPlayer;
import com.mcthepond.champs.library.level.exp.ExpGroup;
import com.mcthepond.champs.library.race.Race;
import com.mcthepond.champs.library.race.RaceHandler;
import com.mcthepond.champs.library.skill.Skill;
import com.mcthepond.champs.library.util.Preconditions;
import com.mcthepond.champs.library.weapon.Weapon;
import com.mcthepond.champs.library.weapon.WeaponAttributes;

import java.sql.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author B2OJustin
 */
public class SQLDataSource implements DataSource {
    
    public static enum SQLDatabaseType {
        MYSQL, SQLITE;
    }

    public static class SQLLoginInfo {

        private boolean locked = false;
        private String hostname, database, username, password;
        private int port;
        private Connection connection;

        public SQLLoginInfo setHost(String hostname) {
            if (!locked) {
                this.hostname = hostname;
            }
            return this;
        }

        public SQLLoginInfo setDatabase(String database) {
            if (!locked) {
                this.database = database;
            }
            return this;
        }

        public SQLLoginInfo setUsername(String username) {
            if (!locked) {
                this.username = username;
            }
            return this;
        }

        public SQLLoginInfo setPassword(String password) {
            if (!locked) {
                this.password = password;
            }
            return this;
        }

        public SQLLoginInfo setPort(int port) {
            if (!locked) {
                this.port = port;
            }
            return this;
        }

        public void lock() {
            locked = true;
        }

        public String getHostname() {
            return hostname;
        }

        public String getDatabase() {
            return database;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public int getPort() {
            return port;
        }

        public Connection getConnection() {
            if (connection == null) {
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    this.connection = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database, this.username, this.password);
                }catch (SQLException e){
                    System.out.println("Could not connect to MySQL server! because: " + e.getMessage());
                }catch (ClassNotFoundException e){
                    System.out.println("JDBC Driver not found!");
                }
            }
            return connection;
        }

    }

    private SQLLoginInfo loginInfo;
    private SQLDatabaseType databaseType;

    public SQLDataSource(SQLDatabaseType databaseType) {
        this.databaseType = databaseType;
        YamlConfiguration config = BaseConfiguration.getInstance().getConfig();
        loginInfo = new SQLLoginInfo().setHost(config.getString("sql.hostname")).setDatabase(config.getString("sql.database")).setUsername(config.getString("sql.username")).setPassword(config.getString("sql.password")).setPort(config.getInt("sql.port"));
        loginInfo.getConnection();
    }

    public SQLDatabaseType getDatabaseType() {
        return this.databaseType;
    }
    
    @Override
    public String getName() {
        switch (this.getDatabaseType()) {
            case MYSQL:
                return "MySQL";
            case SQLITE:
                return "SQLite";
        }
        return "SQL";
    }

    @Override
    public Logger getLogger() {
        return Logger.getLogger(SQLDataSource.class.getName());
    }

    @Override
    public CPlayer loadLPlayer(String name) {
        Preconditions.checkNotNull(name, "Cannot load null name");
        String sql = "SELECT * FROM players WHERE name='" + name.toLowerCase() + "'";
        ResultSet set = runQuery(sql);
        try {
            if (set.getString("name") != null) {
                String race = set.getString("race");
                List<String> description = Arrays.asList((String[])set.getArray("description").getArray());
                String primary = set.getString("primaryclass");
                int primaryLevel = set.getInt("primaryclasslevel");
                double primaryExp = set.getDouble("primaryclassexp");
                String secondary = set.getString("secondaryclass");
                int secondaryLevel = set.getInt("secondaryclasslevel");
                double secondaryExp = set.getDouble("secondaryclassexp");

                CPlayer player = new CPlayer(RaceHandler.getInstance().load(race), CClassHandler.getInstance().load(primary), CClassHandler.getInstance().load(secondary));
                player.setDescription(description);

                player.getPrimaryClassAttributes().getLevel().setLevel(primaryLevel);
                player.getPrimaryClassAttributes().getLevel().setExp(primaryExp);

                player.getSecondaryClassAttributes().getLevel().setLevel(secondaryLevel);
                player.getSecondaryClassAttributes().getLevel().setExp(secondaryExp);
            }
        } catch (SQLException e) {}
        return null;
    }

    @Override
    public void saveLPlayer(CPlayer cplayer) {

    }

    @Override
    public Race loadRace(String name) {
        return null; //TODO loadRace method stub
    }

    @Override
    public CClass loadCClass(String name) {
        return null; //TODO loadLClass method stub
    }

    @Override
    public Skill loadSkill(String name) {
        return null; //TODO loadSkill method stub
    }

    @Override
    public Weapon loadWeapon(String name) {
        return null; //TODO loadWeapon method stub
    }

    @Override
    public BasicCategory<WeaponAttributes> loadWeaponCategory(String name) {
        return null; //TODO loadWeaponCategory method stub
    }

    @Override
    public ExpGroup loadExpGroup(String name) {
        return null; //TODO loadExpGroup method stub
    }

    private ResultSet runPreparedQuery(String query, Object... args){
        PreparedStatement s = null;
        ResultSet res = null;
        try{
            s = loginInfo.getConnection().prepareStatement(query);
            for (int i = 0; i < args.length; i++) {
                s.setObject(i, args[i]);
            }
            res = s.executeQuery(query);
            res.next();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet runQuery(String query){
        Statement s = null;
        ResultSet res = null;
        try{
            s = loginInfo.getConnection().createStatement();
            res = s.executeQuery(query);
            res.next();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    public void runUpdate(String update){
        Statement s = null;
        try{
            s = loginInfo.getConnection().createStatement();
            s.executeUpdate(update);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}
