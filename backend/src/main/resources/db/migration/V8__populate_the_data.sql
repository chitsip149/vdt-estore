INSERT INTO categories (id, name) values
                                      (1, 'Fantasy'),
                                      (2, 'Science Fiction'),
                                      (3, 'Historical Fiction'),
                                      (4, 'Romance'),
                                      (5, 'Mystery/Thriller');


-- Fantasy Books (category_id = 1)
INSERT INTO products (name, price, description, category_id) VALUES
                                                                 ('The Name of the Wind', 250000, 'A legendary tale of a gifted young man who grows to be the most notorious magician his world has ever seen.', 1),
                                                                 ('Mistborn: The Final Empire', 220000, 'A world where ash falls from the sky and mist dominates the night.', 1),
                                                                 ('The Way of Kings', 280000, 'An epic fantasy of war, honor, and shattered worlds.', 1);

-- Science Fiction (category_id = 2)
INSERT INTO products (name, price, description, category_id) VALUES
                                                                 ('Dune', 230000, 'A hero’s journey on the desert planet Arrakis.', 2),
                                                                 ('Ender\'s Game', 200000, 'A young boy trained to fight a war against an alien species.', 2),
                                                                 ('The Martian', 210000, 'A stranded astronaut fights to survive on Mars.', 2);

-- Historical Fiction (category_id = 3)
INSERT INTO products (name, price, description, category_id) VALUES
                                                                 ('The Book Thief', 190000, 'A story narrated by Death, set in Nazi Germany.', 3),
                                                                 ('All the Light We Cannot See', 210000, 'A blind French girl and a German boy during WWII.', 3),
                                                                 ('The Pillars of the Earth', 240000, 'The story of the building of a cathedral in medieval England.', 3);

-- Romance (category_id = 4)
INSERT INTO products (name, price, description, category_id) VALUES
                                                                 ('Pride and Prejudice', 180000, 'The classic tale of love and misunderstanding.', 4),
                                                                 ('The Notebook', 170000, 'A love story spanning decades.', 4),
                                                                 ('It Ends with Us', 200000, 'A powerful story about love and emotional strength.', 4);

-- Mystery/Thriller (category_id = 5)
INSERT INTO products (name, price, description, category_id) VALUES
                                                                 ('Gone Girl', 190000, 'A twisted thriller about a marriage gone wrong.', 5),
                                                                 ('The Girl with the Dragon Tattoo', 210000, 'A journalist and hacker uncover dark secrets.', 5),
                                                                 ('The Silent Patient', 200000, 'A psychological thriller about a woman who stops speaking.', 5);

SET FOREIGN_KEY_CHECKS=0;

INSERT INTO profiles (id, bio, phone_number, date_of_birth, loyalty_points) VALUES
    (1, 'Hi, I\'m Trang! I like to read books.', '0988888888', '2003-09-14', 12);

SET FOREIGN_KEY_CHECKS=1

