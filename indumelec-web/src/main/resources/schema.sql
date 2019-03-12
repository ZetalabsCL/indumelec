-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema indumelec
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema indumelec
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `indumelec` DEFAULT CHARACTER SET utf8 ;
USE `indumelec` ;

-- -----------------------------------------------------
-- Table `indumelec`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `indumelec`.`user` (
                                                `id` INT NOT NULL AUTO_INCREMENT,
                                                `name` VARCHAR(200) NULL,
                                                `mail` VARCHAR(100) NULL,
                                                `password` VARCHAR(100) NULL,
                                                `status` INT NULL,
                                                `updated_by` VARCHAR(100) NULL,
                                                `updated_time` DATETIME NULL,
                                                PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `indumelec`.`business`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `indumelec`.`business` (
                                                    `id` INT NOT NULL AUTO_INCREMENT,
                                                    `taxid` VARCHAR(20) NULL,
                                                    `name` VARCHAR(200) NULL,
                                                    `address` VARCHAR(100) NULL,
                                                    `contact` VARCHAR(200) NULL,
                                                    `phone` VARCHAR(50) NULL,
                                                    `contact_mail` VARCHAR(100) NULL,
                                                    `contact_position` VARCHAR(100) NULL,
                                                    PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `indumelec`.`quote`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `indumelec`.`quote` (
                                                 `id` INT NOT NULL AUTO_INCREMENT,
                                                 `reference` VARCHAR(100) NULL,
                                                 `manufacturing` VARCHAR(100) NULL,
                                                 `date` DATETIME NULL,
                                                 `user_id` INT NOT NULL,
                                                 `business_id` INT NOT NULL,
                                                 PRIMARY KEY (`id`),
                                                 INDEX `fk_quote_user_idx` (`user_id` ASC) VISIBLE,
                                                 INDEX `fk_quote_business1_idx` (`business_id` ASC) VISIBLE,
                                                 CONSTRAINT `fk_quote_user`
                                                   FOREIGN KEY (`user_id`)
                                                     REFERENCES `indumelec`.`user` (`id`)
                                                     ON DELETE NO ACTION
                                                     ON UPDATE NO ACTION,
                                                 CONSTRAINT `fk_quote_business1`
                                                   FOREIGN KEY (`business_id`)
                                                     REFERENCES `indumelec`.`business` (`id`)
                                                     ON DELETE NO ACTION
                                                     ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `indumelec`.`quote_detail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `indumelec`.`quote_detail` (
                                                        `id` INT NOT NULL AUTO_INCREMENT,
                                                        `order` INT NULL,
                                                        `description` VARCHAR(100) NULL,
                                                        `measure` VARCHAR(50) NULL,
                                                        `price` INT NULL,
                                                        `quote_id` INT NOT NULL,
                                                        PRIMARY KEY (`id`),
                                                        INDEX `fk_quote_detail_quote1_idx` (`quote_id` ASC) VISIBLE,
                                                        CONSTRAINT `fk_quote_detail_quote1`
                                                          FOREIGN KEY (`quote_id`)
                                                            REFERENCES `indumelec`.`quote` (`id`)
                                                            ON DELETE NO ACTION
                                                            ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `indumelec`.`quote_history`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `indumelec`.`quote_history` (
                                                         `id` INT NOT NULL,
                                                         `date` DATETIME NULL,
                                                         `status` INT NULL,
                                                         `description` VARCHAR(100) NULL,
                                                         `quote_id` INT NOT NULL,
                                                         `user_id` INT NOT NULL,
                                                         PRIMARY KEY (`id`),
                                                         INDEX `fk_quote_history_quote1_idx` (`quote_id` ASC) VISIBLE,
                                                         INDEX `fk_quote_history_user1_idx` (`user_id` ASC) VISIBLE,
                                                         CONSTRAINT `fk_quote_history_quote1`
                                                           FOREIGN KEY (`quote_id`)
                                                             REFERENCES `indumelec`.`quote` (`id`)
                                                             ON DELETE NO ACTION
                                                             ON UPDATE NO ACTION,
                                                         CONSTRAINT `fk_quote_history_user1`
                                                           FOREIGN KEY (`user_id`)
                                                             REFERENCES `indumelec`.`user` (`id`)
                                                             ON DELETE NO ACTION
                                                             ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `indumelec`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `indumelec`.`role` (
                                                `id` INT NOT NULL,
                                                `role` VARCHAR(100) NULL,
                                                PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `indumelec`.`user_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `indumelec`.`user_role` (
                                                     `user_id` INT NOT NULL,
                                                     `role_id` INT NOT NULL,
                                                     PRIMARY KEY (`user_id`, `role_id`),
                                                     INDEX `fk_role_has_user_user1_idx` (`user_id` ASC) VISIBLE,
                                                     INDEX `fk_role_has_user_role1_idx` (`role_id` ASC) VISIBLE,
                                                     CONSTRAINT `fk_role_has_user_role1`
                                                       FOREIGN KEY (`role_id`)
                                                         REFERENCES `indumelec`.`role` (`id`)
                                                         ON DELETE NO ACTION
                                                         ON UPDATE NO ACTION,
                                                     CONSTRAINT `fk_role_has_user_user1`
                                                       FOREIGN KEY (`user_id`)
                                                         REFERENCES `indumelec`.`user` (`id`)
                                                         ON DELETE NO ACTION
                                                         ON UPDATE NO ACTION)
  ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
