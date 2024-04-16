CREATE TABLE Document (
                          UUID INT AUTO_INCREMENT PRIMARY KEY,
                          NOM_DOCUMENT VARCHAR(255),
                          TYPE_DOCUMENT VARCHAR(100),
                          LINK_DOCUMENT VARCHAR(100),
                          DATE_CREATION DATE,
                          METADATA JSON,
                          HASHED_DOCUMENT TEXT
);