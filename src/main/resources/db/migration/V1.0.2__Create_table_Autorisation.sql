CREATE TABLE Autorisation (
                              ID_AUTORISATION INT AUTO_INCREMENT PRIMARY KEY,
                              DOCUMENT_UUID INT,
                              USER_ID INT,
                              TYPE_AUTORISATION ENUM('Lecture', 'Ecriture', 'Autre'),
                              FOREIGN KEY (DOCUMENT_UUID) REFERENCES Document(UUID) ON DELETE CASCADE,
                              FOREIGN KEY (USER_ID) REFERENCES User(ID_USER)
);