CREATE TYPE order_status_enum AS ENUM ('CREATED', 'IN_PROCESS','SCHEDULED','DELIVERED','CANCELED');
CREATE TYPE ship_type_enum AS ENUM ('INSTORE_PICKUP', 'HOME_DELIVERY');
CREATE TYPE payment_type_enum AS ENUM ('CASH_ON_DELIVERY','ONLINE', 'IN_STORE_PAYMENT');

	
INSERT INTO public.roles (role_id, role_name) VALUES(101,'ADMIN');
INSERT INTO public.roles (role_id, role_name) VALUES(102,'USER');
INSERT INTO public.roles (role_id, role_name) VALUES(103,'STORE');


CREATE TYPE Subscription_Status AS ENUM ('ACTIVE', 'PAUSED', 'CANCELLED');
CREATE TYPE Subscription_Item_Status AS ENUM ('PENDING', 'IN_PROGRESS', 'DELIVERED','PAUSED','CANCELLED');