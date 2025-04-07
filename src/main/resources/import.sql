-- Création du schéma initial pour UnionFlow
-- Version: 1.0.0
-- Date: 2025-04-07

-- Schéma principal
CREATE SCHEMA IF NOT EXISTS unionflow;

-- Extensions
CREATE EXTENSION IF NOT EXISTS unaccent;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Table des membres
CREATE TABLE unionflow.membre (
                                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                  code VARCHAR(50) UNIQUE NOT NULL,
                                  nom VARCHAR(100) NOT NULL,
                                  prenom VARCHAR(100) NOT NULL,
                                  email VARCHAR(150) UNIQUE,
                                  telephone VARCHAR(20),
                                  date_naissance DATE,
                                  adresse TEXT,
                                  profession VARCHAR(100),
                                  photo_url VARCHAR(255),
                                  statut VARCHAR(20) NOT NULL,
                                  date_adhesion DATE NOT NULL,
                                  date_modification TIMESTAMP,
                                  id_parrain UUID REFERENCES unionflow.membre(id),
                                  actif BOOLEAN DEFAULT TRUE,
                                  CONSTRAINT membre_statut_check CHECK (statut IN ('ACTIF', 'INACTIF', 'SUSPENDU', 'RADIE'))
);

COMMENT ON TABLE unionflow.membre IS 'Table des membres de l''association';

-- Autres tables à ajouter...

-- Index pour optimisation des requêtes
CREATE INDEX idx_membre_nom_prenom ON unionflow.membre(nom, prenom);
CREATE INDEX idx_membre_statut ON unionflow.membre(statut);
CREATE INDEX idx_membre_date_adhesion ON unionflow.membre(date_adhesion);