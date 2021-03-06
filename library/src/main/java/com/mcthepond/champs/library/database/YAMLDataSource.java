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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.mcthepond.champs.library.configuration.ChampsConfiguration;
import org.yaml.snakeyaml.Yaml;

import com.mcthepond.champs.library.BasicAttributes;
import com.mcthepond.champs.library.BasicCategory;
import com.mcthepond.champs.library.StatsInfo;
import com.mcthepond.champs.library.armor.Armor;
import com.mcthepond.champs.library.armor.ArmorHandler;
import com.mcthepond.champs.library.armor.ArmorRestricted;
import com.mcthepond.champs.library.armor.ArmorUser;
import com.mcthepond.champs.library.cclass.CClass;
import com.mcthepond.champs.library.cclass.CClassHandler;
import com.mcthepond.champs.library.cclass.CClassRestricted;
import com.mcthepond.champs.library.cclass.CClassUser;
import com.mcthepond.champs.library.cplayer.CPlayer;
import com.mcthepond.champs.library.database.helper.YAMLHelper;
import com.mcthepond.champs.library.level.Level;
import com.mcthepond.champs.library.level.LevelRestricted;
import com.mcthepond.champs.library.level.exp.ExpGroup;
import com.mcthepond.champs.library.level.exp.ExpGroupHandler;
import com.mcthepond.champs.library.level.exp.sources.BlockBreakExpSource;
import com.mcthepond.champs.library.level.exp.sources.BlockPlaceExpSource;
import com.mcthepond.champs.library.level.exp.sources.CraftItemExpSource;
import com.mcthepond.champs.library.level.exp.sources.EnchantExpSource;
import com.mcthepond.champs.library.level.exp.sources.ExpSourceType;
import com.mcthepond.champs.library.level.exp.sources.MobKillExpSource;
import com.mcthepond.champs.library.level.exp.sources.PlayerKillExpSource;
import com.mcthepond.champs.library.level.exp.sources.SkillUseExpSource;
import com.mcthepond.champs.library.race.Race;
import com.mcthepond.champs.library.race.RaceHandler;
import com.mcthepond.champs.library.restriction.BasicRestrictions;
import com.mcthepond.champs.library.restriction.RestrictionHandler;
import com.mcthepond.champs.library.skill.Skill;
import com.mcthepond.champs.library.skill.SkillHandler;
import com.mcthepond.champs.library.util.FileClassLoader;
import com.mcthepond.champs.library.util.FileUtils;
import com.mcthepond.champs.library.weapon.Weapon;
import com.mcthepond.champs.library.weapon.WeaponAttributes;
import com.mcthepond.champs.library.weapon.WeaponCategoryHandler;
import com.mcthepond.champs.library.weapon.WeaponCategoryRestricted;
import com.mcthepond.champs.library.weapon.WeaponHandler;
import com.mcthepond.champs.library.weapon.WeaponRestricted;
import com.mcthepond.champs.library.weapon.WeaponUser;

/**
 * Thread safe class for accessing YAML database.
 *
 * @author B2OJustin
 */
@SuppressWarnings("unchecked")
public class YAMLDataSource implements DataSource {
    private static final Logger logger = Logger.getLogger(YAMLDataSource.class.getName());
    private String configPath;
    private final String RACE_PATH = "races/";
    private final String SKILL_PATH = "skills/";
    private final String PLAYER_PATH = "players/";
    private final String CLASS_PATH = "classes/";
    private final String EXP_PATH = "exp/";
    private final String WEAPON_TYPE_PATH = "weapons/types/";
    private final String WEAPON_PATH = "weapons/";


    @Override
    public String getName() {
        return "YAML";
    }

    public YAMLDataSource(String configPath) {
        this.configPath = configPath;
        if(!configPath.endsWith("/")) configPath = configPath.concat("/");
    }

    @Override
    public Logger getLogger() {
        return YAMLDataSource.logger;
    }

    @Override
    public synchronized CPlayer loadLPlayer(String name) {
        String filePath = configPath + PLAYER_PATH + name.replace(" ", "_") + ".yml";
        CPlayer lPlayer = null;
        try {
            YAMLHelper yamlHelper = new YAMLHelper(filePath);

            lPlayer = new CPlayer(
                    RaceHandler.getInstance().load(yamlHelper.getString("race")),
                    CClassHandler.getInstance().load(yamlHelper.getString("primary-class")),
                    CClassHandler.getInstance().load(yamlHelper.getString("secondary-class"))
            );
            lPlayer.setName(name);
            lPlayer.setDescription(yamlHelper.getStringList("description"));
            lPlayer.getPrimaryClassAttributes().getLevel().setLevel(yamlHelper.getInt("primary-class-level"));
            lPlayer.getPrimaryClassAttributes().getLevel().setExp(yamlHelper.getDouble("primary-class-exp"));
            lPlayer.getSecondaryClassAttributes().getLevel().setLevel(yamlHelper.getInt("secondary-class-level"));
            lPlayer.getSecondaryClassAttributes().getLevel().setExp(yamlHelper.getDouble("secondary-class-exp"));

            for(Map.Entry<String, Integer> entry : yamlHelper.getIntMap("previous-primary-class").entrySet()) {
                lPlayer.addPreviousPrimaryClass(CClassHandler.getInstance().load(entry.getKey()), new Level(entry.getValue()));
            }

            for(Map.Entry<String, Integer> entry : yamlHelper.getIntMap("previous-secondary-class").entrySet()) {
                lPlayer.addPreviousPrimaryClass(CClassHandler.getInstance().load(entry.getKey()), new Level(entry.getValue()));
            }


        } catch (FileNotFoundException ex) {
            logger.warning("Could not find file for player '" + name + "' at " + filePath);
        } catch (ClassCastException ex) {
            logger.warning("You seem to have an error in your yaml. Could not load player '" + name + "'");
        }
        return lPlayer;
    }

    @Override
    public void saveLPlayer(CPlayer lPlayer) {
        LinkedHashMap<String, Object> playerMap = new LinkedHashMap<>(20);
        playerMap.put("name", lPlayer.getName());
        playerMap.put("description", lPlayer.getDescription());
        playerMap.put("race", lPlayer.getRace().getId());
        playerMap.put("primary-class", lPlayer.getPrimaryClass().getId());
        playerMap.put("primary-class-level", lPlayer.getPrimaryClassAttributes().getLevel().getLevel());
        playerMap.put("primary-class-exp", lPlayer.getPrimaryClassAttributes().getLevel().getExp());
        playerMap.put("secondary-class", lPlayer.getSecondaryClass().getId());
        playerMap.put("secondary-class-level", lPlayer.getSecondaryClassAttributes().getLevel().getLevel());
        playerMap.put("secondary-class-exp", lPlayer.getSecondaryClassAttributes().getLevel().getExp());

        // Previous primary classes
        LinkedHashMap<String, Integer> previousPrimaryClasses = new LinkedHashMap<>();
        for(Map.Entry<CClass, Level> entry : lPlayer.getPreviousPrimaryClasses().entrySet()) {
            previousPrimaryClasses.put(entry.getKey().getName(), entry.getValue().getLevel());
        }
        playerMap.put("previous-primary-class", previousPrimaryClasses);

        // Previous secondary classes
        LinkedHashMap<String, Integer> previousSecondaryClasses = new LinkedHashMap<>();
        for(Map.Entry<CClass, Level> entry : lPlayer.getPreviousSecondaryClasses().entrySet()) {
            previousPrimaryClasses.put(entry.getKey().getName(), entry.getValue().getLevel());
        }
        playerMap.put("previous-secondary-class", previousSecondaryClasses);

        try {
            File outputFile = new File(configPath + PLAYER_PATH + lPlayer.getName() + ".yml");
            if(!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
                outputFile.createNewFile();
            }
            Yaml yaml = new Yaml();
            FileUtils.writeStringToFile(outputFile, yaml.dump(playerMap));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized Race loadRace(String id) {
        String filePath = configPath + RACE_PATH + id.replace(" ", "_") + ".yml";
        try {
            Race race;

            // Load custom race file if exists
            Class raceClass = FileClassLoader.load(Race.class, configPath + RACE_PATH, id);
            if(raceClass != null) {
                race = (Race)raceClass.newInstance();
            } else race = new Race();
            race.setId(id);

            YAMLHelper yml = new YAMLHelper(filePath);
            // TODO weapon type bonuses
            for(String mainKey : yml.getKeys("")) {
                switch(mainKey) {
                    case "name":
                        race.setName(yml.getString("name"));
                        break;
                    case "description":
                        race.setDescription(yml.getStringList("description"));
                        break;
                    case "Weapons":
                        RestrictionHandler.getInstance().setWeaponRestrictions(race, loadWeaponRestrictions(race, yml));
                        RestrictionHandler.getInstance().setWeaponTypeRestrictions(race, loadWeaponCategoryRestrictions(race, yml));
                        break;
                    case "Armor":
                        RestrictionHandler.getInstance().setArmorRestrictions(race, loadArmorRestrictions(race, yml));
                        break;
                    case "Class":
                        RestrictionHandler.getInstance().setClassRestrictions(race, loadClassRestrictions(race, yml));
                        break;
                    case "Stats":
                        loadStats(race.getAttributes(), yml);
                }
            }

            return race;

        } catch (FileNotFoundException e) {
            logger.warning("Could not find file for race '" + id + "' at " + filePath);
        } catch (ClassCastException e) {
            logger.warning("You seem to have an error in your yaml. Could not load race '" + id + "'");
        } catch(IllegalAccessException | InstantiationException ignored) {}

        return null;
    }

    @Override
    public synchronized CClass loadCClass(String id) {
        String filePath = configPath + CLASS_PATH + id.replace(" ", "_") + ".yml";
        CClass cClass;
        try {
            // Load custom class file if exists
            Class lClassClass = FileClassLoader.load(Race.class, configPath + CLASS_PATH, id);
            if(lClassClass != null) {
                cClass = (CClass)lClassClass.newInstance();
            } else cClass = new CClass();
            cClass.setId(id);

            YAMLHelper yml = new YAMLHelper(filePath);

            // TODO weapon type bonuses
            for(String mainKey : yml.getKeys("")) {
                switch(mainKey) {
                    case "name":
                        cClass.setName(yml.getString("name"));
                        break;
                    case "description":
                        cClass.setDescription(yml.getStringList("description"));
                        break;
                    case "Weapons":
                        RestrictionHandler.getInstance().setWeaponRestrictions(cClass, loadWeaponRestrictions(cClass, yml));
                        RestrictionHandler.getInstance().setWeaponTypeRestrictions(cClass, loadWeaponCategoryRestrictions(cClass, yml));
                        break;
                    case "Armor":
                        RestrictionHandler.getInstance().setArmorRestrictions(cClass, loadArmorRestrictions(cClass, yml));
                        break;
                    case "Stats":
                        loadStats(cClass.getAttributes(), yml);
                        break;
                    case "Levels":
                        loadLevels(cClass, yml);
                        break;

                }
            }

            return cClass;

        } catch (FileNotFoundException e) {
            logger.warning("Could not find file for class '" + id + "' at " + filePath);
        } catch (ClassCastException e) {
            logger.warning("You seem to have an error in your yaml. Could not load class '" + id + "'");
            e.printStackTrace();
        } catch(IllegalAccessException | InstantiationException ignored) {}

        return null;
    }

    @Override
    public Skill loadSkill(String id) {
        String filePath = configPath + SKILL_PATH + id.replace(" ", "_") + ".yml";
        try {
            Skill skill = new Skill();
            YAMLHelper yml = new YAMLHelper(filePath);
            for(String key : yml.getKeys("")) {
                switch(key.toLowerCase()) {
                    case "name":
                        skill.setName(yml.getString("name"));
                        break;
                    case "description":
                        skill.setDescription(yml.getStringList("description"));
                        break;
                    case "mana-cost":
                        skill.getAttributes().setManaCost(yml.getInt("mana-cost"));
                        break;
                    case "health-cost":
                        skill.getAttributes().setHealthCost(yml.getInt("health-cost"));
                        break;
                    case "stamina-cost":
                        skill.getAttributes().setStaminaCost(yml.getInt("stamina-cost"));
                        break;
                    case "damage":
                        skill.getAttributes().setDamage(yml.getInt("damage"));
                        break;
                    case "cooldown":
                        skill.getAttributes().setCooldownSeconds(yml.getInt("cooldown"));
                        break;
                }
            }
            return skill;
        } catch (FileNotFoundException e) {
            logger.warning("Could not find file for skill '" + id + "' at " + filePath);
        } catch (ClassCastException e) {
            logger.warning("You seem to have an error in your yaml. Could not load skill '" + id + "'");
        }
        return null;
    }

    @Override
    public Weapon loadWeapon(String id) {
        String filePath = configPath + WEAPON_PATH + id.replace(" ", "_") + ".yml";
        try {
            Weapon weapon = new Weapon();
            YAMLHelper yml = new YAMLHelper(filePath);
            loadBasicInfo(weapon.getAttributes(), "", yml);
            for(String key : yml.getKeys("")) {
                switch(key.toLowerCase()) {
                    case "name":
                        weapon.setName(yml.getString("name"));
                        break;
                }
            }
            return weapon;
        } catch (FileNotFoundException e) {
            logger.warning("Could not find file for weapon '" + id + "' at " + filePath);
        } catch (ClassCastException e) {
            logger.warning("You seem to have an error in your yaml. Could not load weapon '" + id + "'");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BasicCategory<WeaponAttributes> loadWeaponCategory(String id) {
        String filePath = configPath + WEAPON_TYPE_PATH + id.replace(" ", "_") + ".yml";
        try {
            BasicCategory<WeaponAttributes> weaponCategory = new BasicCategory<>();
            YAMLHelper yml = new YAMLHelper(filePath);
            loadBasicInfo(weaponCategory.getAttributes(), "", yml);
            for(String key : yml.getKeys("")) {
                switch(key.toLowerCase()) {
                    case "name":
                        weaponCategory.setName(yml.getString("name"));
                        break;
                }
            }
            return weaponCategory;
        } catch (FileNotFoundException e) {
            logger.warning("Could not find file for weapon type '" + id + "' at " + filePath);
        } catch (ClassCastException e) {
            logger.warning("You seem to have an error in your yaml. Could not load weapon type '" + id + "'");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public synchronized ExpGroup loadExpGroup(String id) {
        String filePath = configPath + EXP_PATH + id.replace(" ", "_") + ".yml";
        try {
            ExpGroup expGroup = new ExpGroup(id);
            YAMLHelper yml = new YAMLHelper(filePath);
            for(String typeKey : yml.getKeys("")) {
                switch(ExpSourceType.valueOf(typeKey.toUpperCase())) {
                    case MOB_KILL:
                        for(Map.Entry<String, Double> mobEntry : yml.getDoubleMap(typeKey).entrySet()) {
                            expGroup.add(new MobKillExpSource(mobEntry.getKey()), mobEntry.getValue());
                        }
                        break;
                    case PLAYER_KILL:
                        for(Map.Entry<String, Double> playerEntry : yml.getDoubleMap(typeKey).entrySet()) {
                            expGroup.add(new PlayerKillExpSource(playerEntry.getKey()), playerEntry.getValue());
                        }
                        break;
                    case BLOCK_BREAK:
                        for(Map.Entry<String, Double> blockEntry : yml.getDoubleMap(typeKey).entrySet()) {
                            expGroup.add(new BlockBreakExpSource(blockEntry.getKey()), blockEntry.getValue());
                        }
                        break;
                    case BLOCK_PLACE:
                        for(Map.Entry<String, Double> blockEntry : yml.getDoubleMap(typeKey).entrySet()) {
                            expGroup.add(new BlockPlaceExpSource(blockEntry.getKey()), blockEntry.getValue());
                        }
                        break;
                    case CRAFT:
                        for(Map.Entry<String, Double> blockEntry : yml.getDoubleMap(typeKey).entrySet()) {
                            expGroup.add(new CraftItemExpSource(blockEntry.getKey()), blockEntry.getValue());
                        }
                        break;
                    case ENCHANT:
                        for(Map.Entry<String, Double> blockEntry : yml.getDoubleMap(typeKey).entrySet()) {
                            expGroup.add(new EnchantExpSource(blockEntry.getKey()), blockEntry.getValue());
                        }
                        break;
                    case SKILL:
                        for(Map.Entry<String, Double> skillEntry : yml.getDoubleMap(typeKey).entrySet()) {
                            expGroup.add(new SkillUseExpSource(SkillHandler.getInstance().load(skillEntry.getKey())), skillEntry.getValue());
                        }
                        break;
                }
            }
            return expGroup;
        } catch (FileNotFoundException e) {
            logger.warning("Could not find file for experience source '" + id + "' at " + filePath);
        } catch (ClassCastException e) {
            logger.warning("You seem to have an error in your yaml. Could not load experience source '" + id + "'");
            e.printStackTrace();
        }
        return null;
    }

    public synchronized void loadConfiguration(ChampsConfiguration config, String file) throws FileNotFoundException {
        YAMLHelper yml = new YAMLHelper(configPath + file);

        // Main configuration
        for(String configKey : yml.getKeys("")) {
            switch(configKey.toLowerCase()) {
                case "database-type":
                    config.setDatabaseType(yml.getString(configKey));
                    break;
                case "default-race":
                    config.setDefaultRace(yml.getString(configKey));
                    break;
                case "default-primary-class":
                    config.setDefaultPrimaryClass(yml.getString(configKey));
                    break;
                case "default-secondary-class":
                    config.setDefaultSecondaryClass(yml.getString(configKey));
                    break;
                case "experience-curve":
                    config.setExpCurve(yml.getString(configKey));
                    break;
            }
        }

        // YAML database configuration
        if(config.getDatabaseType().toUpperCase().equals("YAML")) {
            for(String yamlKey : yml.getKeys("YAML")) {
                switch(yamlKey.toLowerCase()) {
                    case "config-path":
                        config.setYamlConfigPath(yml.getString("YAML.config-path"));
                        break;
                }
            }
        }
    }

    // TODO implement yaml configuration saving
    public synchronized YAMLDataSource saveConfiguration(String file) {
        return this;
    }

    protected <T extends BasicAttributes> T loadBasicInfo(T basicInfo, String path, YAMLHelper yml) throws ClassCastException {
        for(String infoKey : yml.getKeys(path)) {
            switch(infoKey.toLowerCase()) {
                case "bonus-defense":
                    int bonusDefense = yml.getInt(path + ".bonus-defense");
                    basicInfo.addDefense(bonusDefense);
                    break;
                case "bonus-weapon-damage":
                    int bonusDamage = yml.getInt(path + ".bonus-weapon-damage");
                    basicInfo.addWeaponDamage(bonusDamage);
                    break;
                case "bonus-minimum-weapon-damage":
                    int bonusMin = yml.getInt(path + ".bonus-minimum-weapon-damage");
                    basicInfo.addBonusMinWeaponDamage(bonusMin);
                    break;
                case "bonus-maximum-weapon-damage":
                    int bonusMax = yml.getInt(path + ".bonus-maximum-weapon-damage");
                    basicInfo.addBonusMaxWeaponDamage(bonusMax);
                    break;
                case "bonus-skill-damage":
                    int skillBonusDamage = yml.getInt(path + ".bonus-skill-damage");
                    basicInfo.addSkillDamage(skillBonusDamage);
                    break;
                case "bonus-minimum-skill-damage":
                    int skillBonusMinDamage = yml.getInt(path + ".bonus-minimum-skill-damage");
                    basicInfo.addBonusMinSkillDamage(skillBonusMinDamage);
                    break;
                case "bonus-maximum-skill-damage":
                    int skillBonusMaxDamage = yml.getInt(path + ".bonus-maximum-skill-damage");
                    basicInfo.addBonusMaxSkillDamage(skillBonusMaxDamage);
                    break;
                case "bonus-stamina":
                    int bonusStamina = yml.getInt(path + ".bonus-stamina");
                    basicInfo.addStamina(bonusStamina);
                    break;
                case "bonus-health":
                    int bonusHealth = yml.getInt(path + ".bonus-health");
                    basicInfo.addHealth(bonusHealth);
                    break;
                case "bonus-mana":
                    int bonusMana = yml.getInt(path + ".bonus-mana");
                    basicInfo.addMana(bonusMana);
                    break;

                // Level restricted
                case "required-level":
                    if(basicInfo instanceof LevelRestricted) {
                        int requiredLevel = yml.getInt(path + ".required-level");
                        RestrictionHandler.getInstance().getLevelRestrictions((LevelRestricted)basicInfo).setMinLevel(requiredLevel);
                    }
                    break;
                case "maximum-level":
                    if(basicInfo instanceof  LevelRestricted) {
                        int maximumLevel = yml.getInt(path + ".maximum-level");
                        RestrictionHandler.getInstance().getLevelRestrictions((LevelRestricted)basicInfo).setMaxLevel(maximumLevel);
                    }
                    break;
            }
        }
        return basicInfo;
    }

    protected synchronized StatsInfo loadStats(StatsInfo stats, YAMLHelper yml) {
        if(stats == null) return null;
        for(String statKey : yml.getKeys("Stats")) {
            switch(statKey.toLowerCase()) {
                case "health-per-level":
                    int healthPerLevel = yml.getInt("Stats.health-per-level");
                    stats.setHealthPerLevel(healthPerLevel);
                    break;
                case "mana-per-level":
                    int manaPerLevel = yml.getInt("Stats.mana-per-level");
                    stats.setManaPerLevel(manaPerLevel);
                    break;
                case "stamina-per-level":
                    int staminaPerLevel = yml.getInt("Stats.stamina-per-level");
                    stats.setStaminaPerLevel(staminaPerLevel);
                    break;
            }
        }
        // Basic bonuses
        if(stats instanceof BasicAttributes) {
            loadBasicInfo((BasicAttributes)stats, "Stats", yml);
        }
        return stats;
    }

    protected synchronized CClass loadLevels(CClass cClass, YAMLHelper yml) {
        if(cClass == null) return null;
        for (String levelKey : yml.getKeys("Levels")) {
            switch(levelKey.toLowerCase()) {
                case "experience-sources":
                    for(String sourceKey : yml.getKeys("Levels.experience-sources")) {
                            float expModifier = 1f;
                            for(String modKey : yml.getKeys(String.format("Levels.experience-sources.%s", sourceKey))) {
                                switch(modKey.toLowerCase()) {
                                    case "modifier":
                                        expModifier = yml.getFloat(String.format("Levels.experience-sources.%s.%s", sourceKey, modKey));
                                        break;
                                 }
                            }
                        cClass.addExpGroup(ExpGroupHandler.getInstance().load(sourceKey), expModifier);
                    }
                    break;
                case "max-level":
                    // Sets mastery level to max level if necessary
                    int maxLevel = yml.getInt("Levels.max-level");
                    cClass.getAttributes().setMaxLevel(new Level(maxLevel));
                    if(cClass.getAttributes().getMasteryLevel().equals(new Level(0))) {
                        cClass.getAttributes().setMasteryLevel(new Level(maxLevel));
                    }
                    break;
                case "mastery-level":
                    cClass.getAttributes().setMasteryLevel(new Level(yml.getInt("Levels.mastery-level")));
                    break;
                case "experience-curve":
                    //TODO experience curve implementation
                    break;
            }


        }
        return cClass;
    }

    protected synchronized BasicRestrictions<Weapon> loadWeaponRestrictions(WeaponRestricted restricted, YAMLHelper yml) {
        BasicRestrictions<Weapon> weaponRestrictions = new BasicRestrictions<>();
        for(String wepKey : yml.getKeys("Weapons")) {
            switch(wepKey.toLowerCase()) {
                case "default":
                    if(yml.getString("Weapons.default").equals("allow")) {
                        weaponRestrictions.setDefault(true);
                    }
                    else weaponRestrictions.setDefault(false);
                    break;
                case "permitted-weapon":
                    for(String wepID : yml.getKeys("Weapons.permitted-weapon")) {
                        Weapon weapon = WeaponHandler.getInstance().load(wepID);
                        weaponRestrictions.setAllowed(weapon, true);
                        if(restricted instanceof WeaponUser) {
                            loadBasicInfo(((WeaponUser)restricted).getWeaponAttributes(weapon), String.format("Weapons.permitted-weapon.%s", wepID), yml);
                        }
                    }
                    break;
                case "restricted-weapon":
                    for(String wepID : yml.getStringList("Weapons.restricted-weapon")) {
                        Weapon weapon = WeaponHandler.getInstance().load(wepID);
                        weaponRestrictions.setAllowed(weapon, false);
                    }
                    break;
            }
        }
        return weaponRestrictions;
    }

    protected synchronized BasicRestrictions<BasicCategory<WeaponAttributes>> loadWeaponCategoryRestrictions(WeaponCategoryRestricted restricted, YAMLHelper yml) {
        BasicRestrictions<BasicCategory<WeaponAttributes>> weaponCategoryRestrictions = new BasicRestrictions<>();
        for(String typeKey : yml.getKeys("Weapons")) {
            switch(typeKey.toLowerCase()) {
                case "permitted-weapon-type":
                    for(String typeID : yml.getKeys("Weapons.permitted-weapon-type")) {
                        BasicCategory<WeaponAttributes> weaponCategory = WeaponCategoryHandler.getInstance().load(typeID);
                        weaponCategoryRestrictions.setAllowed(weaponCategory, true);
                        if(restricted instanceof WeaponUser) {
                            loadBasicInfo(((WeaponUser)restricted).getWeaponCategoryAttributes(weaponCategory), String.format("Weapons.permitted-weapon.%s", typeID), yml);
                        }
                    }
                    break;
                case "restricted-weapon-type":
                    for(String typeID : yml.getStringList("Weapons.restricted-weapon-type")) {
                        BasicCategory<WeaponAttributes> weaponCategory = WeaponCategoryHandler.getInstance().load(typeID);
                        weaponCategoryRestrictions.setAllowed(weaponCategory, false);
                    }
                    break;
            }
        }
        return weaponCategoryRestrictions;
    }

    protected BasicRestrictions<Armor> loadArmorRestrictions(ArmorRestricted restricted, YAMLHelper yml) {
        BasicRestrictions<Armor> restrictions = new BasicRestrictions<>();
        for(String armorKey : yml.getKeys("Armor")) {
            switch(armorKey.toLowerCase()) {
                case "default":
                    if(yml.getString("Armor.default").equals("allow")) {
                        restrictions.setDefault(true);
                    }
                    else restrictions.setDefault(false);
                    break;
                case "permitted-armor":
                    for(String armorID : yml.getKeys("Armor.permitted-armor")) {
                        Armor armor = ArmorHandler.getInstance().get(armorID);
                        if(armor != null) {
                            restrictions.setAllowed(armor, true);
                            if(restricted instanceof ArmorUser) {
                                loadBasicInfo(((ArmorUser)restricted).getArmorAttributes(armor), String.format("Armor.permitted-armor.%s", armorID), yml);
                            }
                        }
                    }
                    break;
                case "restricted-armor":
                    for(String armorID : yml.getStringList("Armor.restricted-armor")) {
                        Armor armor = ArmorHandler.getInstance().get(armorID);
                        restrictions.setAllowed(armor, false);
                    }
            }
        }
        return restrictions;
    }

    protected BasicRestrictions<CClass> loadClassRestrictions(CClassRestricted restricted, YAMLHelper yml) {
        BasicRestrictions<CClass> restrictions = new BasicRestrictions<CClass>();
        for(String classKey : yml.getKeys("Class")) {
            switch(classKey.toLowerCase()) {
                case "default":
                    if(yml.getString("Class.default").equals("allow")) {
                        restrictions.setDefault(true);
                    }
                    else restrictions.setDefault(false);
                    break;
                case "permitted-class":
                    for(String classID : yml.getKeys("Class.permitted-class")) {
                        CClass cClass = CClassHandler.getInstance().get(classID);
                        if(cClass != null) {
                            restrictions.setAllowed(cClass, true);
                            if(restricted instanceof CClassUser) {
                                loadBasicInfo(((CClassUser)restricted).getCClassAttributes(cClass), String.format("Class.permitted-class.%s", classID), yml);
                            }
                        }
                    }
                    break;
                case "restricted-class":
                    for(String classID : yml.getKeys("Class.restricted-class")) {
                        CClass cClass = CClassHandler.getInstance().get(classID);
                        if(cClass != null) {
                            restrictions.setAllowed(cClass, false);
                        }
                    }
            }
        }
        return restrictions;
    }
}
