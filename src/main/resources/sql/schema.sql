-- src/main/resources/sql/schema.sql

-- Table des utilisateurs
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    created_at TEXT NOT NULL
);

-- Table des bibliothèques utilisateur (relation user-comics)
CREATE TABLE IF NOT EXISTS user_libraries (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    comic_id INTEGER NOT NULL,         -- ID du comic dans la base de données de l'API
    comic_title TEXT NOT NULL,      -- infos utilisées pour comicInfoUI
    comic_read_status INTEGER NOT NULL, --valeur donnant l'état de lecture d'un comic
    in_library INTEGER NOT NULL,
    in_favorite INTEGER NOT NULL,
    in_wish_list INTEGER NOT NULL,
    rating INTEGER NOT NULL,
    comic_image_url TEXT,
    comic_aliases TEXT,
    comic_detail_url TEXT,
    comic_character_credits TEXT,
    comic_publication_date TEXT,
    comic_deck TEXT,
    FOREIGN KEY(user_id) REFERENCES users(id)
    UNIQUE(user_id, comic_id)       -- Empêche d'ajouter plusieurs fois le même comic à la bibliothèque
);





-- Index sur le nom d'utilisateur pour optimiser les recherches
CREATE INDEX IF NOT EXISTS idx_username ON users(username);
-- Index sur les bibliothèques utilisateur
CREATE INDEX IF NOT EXISTS idx_user_libraries ON user_libraries(user_id);

