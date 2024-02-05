CREATE TYPE order_status_enum AS ENUM ('CREATED', 'PROCESSING','SHIPPED','DELIVERED','CANCELED');
CREATE TYPE ship_type_enum AS ENUM ('INSTORE_PICKUP', 'HOME_DELIVERY');
CREATE TYPE payment_type_enum AS ENUM ('ONLINE', 'IN_STORE_PAYMENT');
	
INSERT INTO public.roles (role_id, role_name) VALUES(101,'ADMIN');
INSERT INTO public.roles (role_id, role_name) VALUES(102,'USER');
INSERT INTO public.roles (role_id, role_name) VALUES(103,'STORE');


INSERT INTO public.stores(store_id, email, "name", phone_name, user_id) values(1, 'vattemshiva467@gmail.com', 'Gelato Creamery', '7512712044', 1);

INSERT INTO public.stores(store_id, email, "name", phone_name, user_id) values(2, 'vattemshiva467@gmail.com', 'Tasty Eats', '7512712044', 1);

INSERT INTO public.categories (category_id, category_name, store_id) VALUES(1, 'Non Veg Menu', 1);
INSERT INTO public.categories (category_id, category_name, store_id) VALUES(2, 'Veg Menu', 2);

INSERT INTO public.products (product_id, description, features, image, product_name, category_id) VALUES(1, 'Chicken Sandwhich', NULL, NULL, 'Chicken Sandwhich', 1);
INSERT INTO public.products (product_id, description, features, image, product_name, category_id) VALUES(2, 'Chicken Burger', NULL, NULL, 'Chicken Burger', 1);
INSERT INTO public.products (product_id, description, features, image, product_name, category_id) VALUES(3, 'Paneer Sandwhich', NULL, NULL, 'Paneer Sandwhich', 2);
INSERT INTO public.products (product_id, description, features, image, product_name, category_id) VALUES(4, 'Cheese Burger', NULL, NULL, 'Veg Burger', 2);


INSERT INTO public.skus (sku_id, description, image, list_price, "name", quantity, sale_price, product_id, store_id) VALUES(1000, 'Servers 1 Adult + 1 Child', NULL, 10.0, 'Lunch Mean With Chicken Sandwhich ', 10, 8.0, 1, 1);
INSERT INTO public.skus (sku_id, description, image, list_price, "name", quantity, sale_price, product_id, store_id) VALUES(1001, 'Servers 2 Adult + 2 Child', NULL, 25.0, 'Group Lunch Meanl With Chicken Sandwhich ', 10, 20.0, 1, 1);


