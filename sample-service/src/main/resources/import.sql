INSERT INTO PRODUCTS (NAME, PRICE, DESCRIPTION, AVAILABILITY, PRODUCT_ID) VALUES ('Code Complete, Second Edition', 49.99, 'Take a strategic approach to software constructionand produce superior productswith this fully updated edition of Steve McConnells critically praised and award-winning guide to software development best practices. ', 'In Stock', 1)
	   			
INSERT INTO BOOKS (NUMBER_OF_PAGES, ISBN, PUBLISH_DATE, AUTHORS, BOOK_ID) VALUES (960, '0735619670', SYSDATE, 'Steve McConnell', 1) 

INSERT INTO PRODUCTS (NAME, PRICE, DESCRIPTION, AVAILABILITY, PRODUCT_ID) VALUES ('Design Patterns: Elements of Reusable Object-Oriented Software', 54.99, 'Design Patterns is a modern classic in the literature of object-oriented development, offering timeless and elegant solutions to common problems in software design.', 'In Stock', 2)
	   			
INSERT INTO BOOKS (NUMBER_OF_PAGES, ISBN, PUBLISH_DATE, AUTHORS, BOOK_ID) VALUES (395, '0201633612', SYSDATE, 'Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides', 2) 

INSERT INTO PRODUCTS (NAME, PRICE, DESCRIPTION, AVAILABILITY, PRODUCT_ID) VALUES ('Apple MacBook Pro MA092LL', 2799.98, 'Powered by a dual-core Intel engine. Up to five times the speed of the PowerBook G4. Eight times the graphics bandwidth. With built-in iSight for instant video conferencing on the move.', 'In Stock', 3)
       
INSERT INTO COMPUTERS (MEMORY, MONITOR_SIZE, COMPUTER_ID) VALUES (1024, 17, 3)

INSERT INTO PRODUCTS (NAME, PRICE, DESCRIPTION, AVAILABILITY, PRODUCT_ID) VALUES ('Norton Systemworks 2006 Premier', 99.99, 'Norton Systemworks 2006 Premier offers some of the industry''s most comprehensive solutions that are designed not only to keep viruses and hackers from reaching your files, but that will also keep your computer running smoothly and efficiently.', 'In Stock', 4)
	   			
INSERT INTO SOFTWARES (PLATFORMS, MEDIA, ITEM_QUANTITY, SOFTWARE_ID) VALUES ('Windows XP,Windows 2000', 'CD-ROM', 1, 4)

INSERT INTO CUSTOMERS (FIRST_NAME, LAST_NAME, EMAIL_ADDRESS, CUSTOMER_ID) VALUES ('Chunyun', 'Zhao', 'czhao_test@gmail.com', 1)

INSERT INTO PAYMENT_METHODS (CARD_NUMBER, VERIFICATION_CODE, EXPIRATION_DATE, DESCRIPTION, PAYMENT_TYPE, CUSTOMER_ID, SORT_ORDER, PAYMENT_METHOD_ID) VALUES ('1111222233334444', '321', '2008-08-01', 'Citi Diamond Preferred Rewards', 'Credit Card', 1, 1, 1)

INSERT INTO PAYMENT_METHODS (CARD_NUMBER, VERIFICATION_CODE, EXPIRATION_DATE, DESCRIPTION, PAYMENT_TYPE, CUSTOMER_ID, SORT_ORDER, PAYMENT_METHOD_ID) VALUES ('4444333322221111', '321', '2008-03-01', 'Bank of America Check Card', 'Debit Card', 1, 2, 2)