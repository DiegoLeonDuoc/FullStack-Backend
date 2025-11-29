INSERT INTO users (rut, age, first_name, last_name, phone, email, password_hash, created_at, role, is_active) VALUES
('11111111-1', 30, 'John', 'Doe', '123456789', 'john.doe@example.com', 'hashedpassword', CURRENT_TIMESTAMP, 'USER', TRUE),
('22222222-2', 28, 'Jane', 'Smith', '987654321', 'jane.smith@example.com', 'hashedpassword', CURRENT_TIMESTAMP, 'ADMIN', TRUE);

INSERT INTO artists (artist_name, country, founded_year, genre, is_active) VALUES
('The Beatles', 'UK', 1960, 'Rock', TRUE),
('Pink Floyd', 'UK', 1965, 'Progressive Rock', TRUE);

INSERT INTO labels (label_name, country, founded_year, website, contact_email) VALUES
('Apple Records', 'UK', 1968, 'https://www.apple.com/music', 'contact@applemusic.com'),
('Columbia Records', 'USA', 1887, 'https://www.columbiarecords.com', 'info@columbiarecords.com');

INSERT INTO products (product_id, title, artist_id, label_id, format_name, format_type, image_url, release_year, description, price, stock_quantity, avg_rating, rating_count, is_available, created_at, updated_at) VALUES
('abbey-road-vinilo', 'Abbey Road', 1, 1, 'Vinilo', 'VINYL', 'https://example.com/abbey-road.jpg', 1969, 'Classic Beatles album', 25000, 50, 4.8, 1200, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('dark-side-cd', 'The Dark Side of the Moon', 2, 2, 'CD', 'CD', 'https://example.com/darkside.jpg', 1973, 'Iconic Pink Floyd album', 15000, 70, 4.9, 1500, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO orders (customer_id, product_id, artist_id, label_id, responsible_user_id, quantity, total_price, status, order_date, updated_at) VALUES
(1, 'abbey-road-vinilo', 1, 1, 2, 2, 50000, 'CREATED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'dark-side-cd', 2, 2, 2, 1, 15000, 'SHIPPED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
