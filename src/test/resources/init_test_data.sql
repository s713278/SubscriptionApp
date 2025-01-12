-- DROP SCHEMA public;

--CREATE SCHEMA public AUTHORIZATION pg_database_owner;

--COMMENT ON SCHEMA public IS 'standard public schema';

-- DROP TYPE public."approvalstatus";

CREATE TYPE public."approvalstatus" AS ENUM (
	'APPROVED',
	'PENDING',
	'REJECTED');

-- DROP TYPE public."delivery_mode_enum";

CREATE TYPE public."delivery_mode_enum" AS ENUM (
	'FIXED',
	'FLEXIBLE');

-- DROP TYPE public."deliverymode";

CREATE TYPE public."deliverymode" AS ENUM (
	'FIXED',
	'FLEXIBLE');

-- DROP TYPE public."gender";

CREATE TYPE public."gender" AS ENUM (
	'MAN',
	'OTHER',
	'WOMAN');

-- DROP TYPE public."gender_enum";

CREATE TYPE public."gender_enum" AS ENUM (
	'MAN',
	'WOMAN',
	'OTHER');

-- DROP TYPE public."order_status_enum";

CREATE TYPE public."order_status_enum" AS ENUM (
	'PENDING',
	'SCHEDULED',
	'IN_PROCESS',
	'DELIVERED',
	'CANCELED');

-- DROP TYPE public."orderstatus";

CREATE TYPE public."orderstatus" AS ENUM (
	'CANCELED',
	'DELIVERED',
	'IN_PROCESS',
	'PENDING',
	'SCHEDULED');

-- DROP TYPE public."payment_status_enum";

CREATE TYPE public."payment_status_enum" AS ENUM (
	'DUE',
	'PAID');

-- DROP TYPE public."payment_type_enum";

CREATE TYPE public."payment_type_enum" AS ENUM (
	'CASH_ON_DELIVERY',
	'ONLINE',
	'IN_STORE_PAYMENT');

-- DROP TYPE public."paymentstatus";

CREATE TYPE public."paymentstatus" AS ENUM (
	'DUE',
	'PAID');

-- DROP TYPE public."paymenttype";

CREATE TYPE public."paymenttype" AS ENUM (
	'CASH_ON_DELIVERY',
	'IN_STORE_PAYMENT',
	'ONLINE');

-- DROP TYPE public."ship_type_enum";

CREATE TYPE public."ship_type_enum" AS ENUM (
	'INSTORE_PICKUP',
	'HOME_DELIVERY');

-- DROP TYPE public."sku_type";

CREATE TYPE public."sku_type" AS ENUM (
	'ITEM',
	'SERVICE');

-- DROP TYPE public."skutype";

CREATE TYPE public."skutype" AS ENUM (
	'DIGITAL',
	'ITEM',
	'SERVICE');

-- DROP TYPE public."sky_type";

CREATE TYPE public."sky_type" AS ENUM (
	'ITEM',
	'SERVICE',
	'DIGITAL');

-- DROP TYPE public."subfrequency";

CREATE TYPE public."subfrequency" AS ENUM (
	'ALTERNATE_DAY',
	'CUSTOM',
	'DAILY',
	'MONTHLY',
	'ONE_TIME',
	'WEEKLY');

-- DROP TYPE public."subscription_frequency";

CREATE TYPE public."subscription_frequency" AS ENUM (
	'ONE_TIME',
	'DAILY',
	'WEEKLY',
	'CUSTOM',
	'ALTERNATE_DAY',
	'MONTHLY');

-- DROP TYPE public."subscription_status";

CREATE TYPE public."subscription_status" AS ENUM (
	'ACTIVE',
	'PAUSED',
	'CANCELLED',
	'EXPIRED',
	'DELETED',
	'PENDING');

-- DROP TYPE public."subscriptionfrequency";

CREATE TYPE public."subscriptionfrequency" AS ENUM (
	'ALTERNATE_DAY',
	'CUSTOM',
	'DAILY',
	'ONE_TIME',
	'WEEKLY');

-- DROP TYPE public."subscriptionplan";

CREATE TYPE public."subscriptionplan" AS ENUM (
	'ALTERNATE_DAY',
	'CUSTOM',
	'DAILY',
	'FRIDAY',
	'MONDAY',
	'ONE_TIME',
	'SATURDAY',
	'SUNDAY',
	'THURSDAY',
	'TUESDAY',
	'WEDNESDAY',
	'WEEKLY');

-- DROP TYPE public."subscriptionstatus";

CREATE TYPE public."subscriptionstatus" AS ENUM (
	'ACTIVE',
	'CANCELLED',
	'DELETED',
	'EXPIRED',
	'PAUSED',
	'PENDING');

-- DROP TYPE public."user_type";

CREATE TYPE public."user_type" AS ENUM (
	'USER',
	'VENDOR',
	'ADMIN');

-- DROP TYPE public."usertype";

CREATE TYPE public."usertype" AS ENUM (
	'ADMIN',
	'CUSTOMER_CARE',
	'USER',
	'VENDOR',
	'VENDOR_STAFF');

-- DROP TYPE public."vendor_status_enum";

CREATE TYPE public."vendor_status_enum" AS ENUM (
	'NEW',
	'ACTIVE',
	'DELETED',
	'SUSPENDED',
	'INACTIVE');

-- DROP TYPE public."verification_status_enum";

CREATE TYPE public."verification_status_enum" AS ENUM (
	'PENDING',
	'APPROVED',
	'REJECTED');


-- DROP SEQUENCE carts_seq;

CREATE SEQUENCE carts_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE order_status_history_seq;

CREATE SEQUENCE order_status_history_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE product_seq;

CREATE SEQUENCE product_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE products_seq;

CREATE SEQUENCE products_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE sku_seq;

CREATE SEQUENCE sku_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE skus_seq;

CREATE SEQUENCE skus_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE subscription_seq;

CREATE SEQUENCE subscription_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_addresses_address_id_seq;

CREATE SEQUENCE tb_addresses_address_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_cart_items_id_seq;

CREATE SEQUENCE tb_cart_items_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_cart_seq;

CREATE SEQUENCE tb_cart_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_carts_seq;

CREATE SEQUENCE tb_carts_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_catalog_catalog_id_seq;

CREATE SEQUENCE tb_catalog_catalog_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_category_id_seq;

CREATE SEQUENCE tb_category_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_category_vendor_id_seq;

CREATE SEQUENCE tb_category_vendor_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_customer_id_seq;

CREATE SEQUENCE tb_customer_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_customer_seq;

CREATE SEQUENCE tb_customer_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_order_items_id_seq;

CREATE SEQUENCE tb_order_items_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_order_order_id_seq;

CREATE SEQUENCE tb_order_order_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_order_order_id_seq1;

CREATE SEQUENCE tb_order_order_id_seq1
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_order_status_history_seq;

CREATE SEQUENCE tb_order_status_history_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_payment_payment_id_seq;

CREATE SEQUENCE tb_payment_payment_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_price_list_seq;

CREATE SEQUENCE tb_price_list_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_product_seq;

CREATE SEQUENCE tb_product_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_product_vendor_id_seq;

CREATE SEQUENCE tb_product_vendor_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_products_seq;

CREATE SEQUENCE tb_products_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_role_role_id_seq;

CREATE SEQUENCE tb_role_role_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_service_attributes_seq;

CREATE SEQUENCE tb_service_attributes_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_shipping_id_seq;

CREATE SEQUENCE tb_shipping_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_sku_price_id_seq;

CREATE SEQUENCE tb_sku_price_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_sku_seq;

CREATE SEQUENCE tb_sku_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_sku_sub_plan_id_seq;

CREATE SEQUENCE tb_sku_sub_plan_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_skus_seq;

CREATE SEQUENCE tb_skus_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_sub_plan_id_seq;

CREATE SEQUENCE tb_sub_plan_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_subscription_item_seq;

CREATE SEQUENCE tb_subscription_item_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_subscription_seq;

CREATE SEQUENCE tb_subscription_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_vendor_id_seq;

CREATE SEQUENCE tb_vendor_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_vendor_legal_details_id_seq;

CREATE SEQUENCE tb_vendor_legal_details_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE tb_vendor_sku_price_seq;

CREATE SEQUENCE tb_vendor_sku_price_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE vendor_sku_price_seq;

CREATE SEQUENCE vendor_sku_price_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;-- public.tb_addresses definition

-- Drop table

-- DROP TABLE tb_addresses;

CREATE TABLE tb_addresses (
	address_id bigserial NOT NULL,
	address1 varchar(255) NULL,
	address2 varchar(255) NULL,
	city varchar(255) NULL,
	country varchar(255) NULL,
	pincode varchar(6) NULL,
	state varchar(255) NULL,
	CONSTRAINT tb_addresses_pkey PRIMARY KEY (address_id)
);


-- public.tb_category definition

-- Drop table

-- DROP TABLE tb_category;

CREATE TABLE tb_category (
	id bigserial NOT NULL,
	description varchar(255) NULL,
	"name" varchar(255) NULL,
	image_path varchar(255) NULL,
	"type" varchar NULL,
	CONSTRAINT tb_category_pkey PRIMARY KEY (id)
);


-- public.tb_customer definition

-- Drop table

-- DROP TABLE tb_customer;

CREATE TABLE tb_customer (
	id bigserial NOT NULL,
	created_date timestamp(6) NULL,
	delivery_address jsonb NULL,
	email varchar(255) NULL,
	email_verified bool NULL,
	first_name varchar(20) NULL,
	last_name varchar(255) NULL,
	mobile int8 NOT NULL,
	mobile_verified bool DEFAULT false NULL,
	reg_device varchar(255) NULL,
	reg_source varchar(255) NULL,
	is_active bool DEFAULT true NOT NULL,
	created_by varchar(50) NULL,
	last_modified_date timestamp(6) NULL,
	last_modified_by varchar(50) NULL,
	delivery_instructions jsonb NULL,
	mobile_verified_timestamp timestamp(6) NULL,
	date_of_birth date NULL,
	"gender" public."gender_enum" NULL,
	preferences jsonb NULL,
	country_code varchar(255) DEFAULT ((+ 91)) NULL,
	"type" public."user_type" NULL,
	CONSTRAINT tb_customer_pkey PRIMARY KEY (id),
	CONSTRAINT uk_a9xeibptr987g1od1c430m1w9 UNIQUE (email),
	CONSTRAINT uk_mobile_number UNIQUE (mobile)
);


-- public.tb_payment definition

-- Drop table

-- DROP TABLE tb_payment;

CREATE TABLE tb_payment (
	payment_id bigserial NOT NULL,
	payment_method public."payment_type_enum" NULL,
	CONSTRAINT tb_payment_payment_method_check CHECK ((payment_method = ANY (ARRAY['CASH_ON_DELIVERY'::payment_type_enum, 'ONLINE'::payment_type_enum, 'IN_STORE_PAYMENT'::payment_type_enum]))),
	CONSTRAINT tb_payment_pkey PRIMARY KEY (payment_id)
);


-- public.tb_role definition

-- Drop table

-- DROP TABLE tb_role;

CREATE TABLE tb_role (
	role_id bigserial NOT NULL,
	role_name varchar(255) NULL,
	CONSTRAINT tb_role_pkey PRIMARY KEY (role_id)
);


-- public.tb_sub_plan definition

-- Drop table

-- DROP TABLE tb_sub_plan;

CREATE TABLE tb_sub_plan (
	id bigserial NOT NULL,
	description varchar(255) NULL,
	"name" varchar(255) NULL,
	frequency public."subscription_frequency" NULL,
	delivery_mode public."delivery_mode_enum" NULL,
	subscription_eligible bool DEFAULT true NULL,
	CONSTRAINT tb_sub_plan_pkey PRIMARY KEY (id),
	CONSTRAINT tb_sub_plan_unique UNIQUE (frequency, delivery_mode)
);
CREATE INDEX tb_sub_plan_frequency_idx ON public.tb_sub_plan USING btree (frequency, delivery_mode);


-- public.tb_cart definition

-- Drop table

-- DROP TABLE tb_cart;

CREATE TABLE tb_cart (
	id int8 NOT NULL,
	total_price numeric(38, 2) NULL,
	user_id int8 NULL,
	CONSTRAINT tb_cart_pkey PRIMARY KEY (id),
	CONSTRAINT uk_d29aq52yuxvaflsnosxaoe89b UNIQUE (user_id),
	CONSTRAINT fkg5gkt4jlcr6sa8q1b6hftnbn1 FOREIGN KEY (user_id) REFERENCES tb_customer(id) ON DELETE CASCADE
);


-- public.tb_product definition

-- Drop table

-- DROP TABLE tb_product;

CREATE TABLE tb_product (
	id int8 NOT NULL,
	description varchar(255) NULL,
	image_path varchar(255) NULL,
	"name" varchar(255) NULL,
	category_id int8 NULL,
	created_date timestamp(6) NULL,
	last_modified_date timestamp(6) NULL,
	created_by varchar(50) NULL,
	last_modified_by varchar(50) NULL,
	CONSTRAINT tb_product_pkey PRIMARY KEY (id),
	CONSTRAINT fk8i0sq9mfbpsrabrm2pum9fspo FOREIGN KEY (category_id) REFERENCES tb_category(id)
);


-- public.tb_shipping definition

-- Drop table

-- DROP TABLE tb_shipping;

CREATE TABLE tb_shipping (
	id bigserial NOT NULL,
	shipping_method public."ship_type_enum" NULL,
	shipping_id int8 NULL,
	CONSTRAINT tb_shipping_pkey PRIMARY KEY (id),
	CONSTRAINT tb_shipping_shipping_method_check CHECK ((shipping_method = ANY (ARRAY['INSTORE_PICKUP'::ship_type_enum, 'HOME_DELIVERY'::ship_type_enum]))),
	CONSTRAINT uk_mutiieemjwe7hcd53qe3nvir UNIQUE (shipping_id),
	CONSTRAINT fkijjfkufjdwq7h6ntarf038rbs FOREIGN KEY (shipping_id) REFERENCES tb_addresses(address_id)
);


-- public.tb_user_roles definition

-- Drop table

-- DROP TABLE tb_user_roles;

CREATE TABLE tb_user_roles (
	user_id int8 NOT NULL,
	role_id int8 NOT NULL,
	CONSTRAINT tb_user_roles_pkey PRIMARY KEY (user_id, role_id),
	CONSTRAINT fkft1jmfcluls775jqp5142wvl8 FOREIGN KEY (role_id) REFERENCES tb_role(role_id),
	CONSTRAINT fknq9blab4wxb1gamm6rev4gmcf FOREIGN KEY (user_id) REFERENCES tb_customer(id) ON DELETE CASCADE
);


-- public.tb_vendor definition

-- Drop table

-- DROP TABLE tb_vendor;

CREATE TABLE tb_vendor (
	id bigserial NOT NULL,
	business_address jsonb NULL,
	business_name varchar(255) NOT NULL,
	contact_number varchar(255) NULL,
	created_date timestamptz(6) DEFAULT CURRENT_TIMESTAMP NULL,
	communication_email varchar(255) NULL,
	last_modified_date timestamptz(6) NULL,
	owner_name varchar(255) NULL,
	service_area jsonb NULL,
	user_id int8 NULL,
	status public."vendor_status_enum" NULL,
	last_modified_by varchar(50) NULL,
	created_by varchar(50) NULL,
	approval_status public."verification_status_enum" NULL,
	banner_image varchar(255) NULL,
	business_type varchar(255) NULL,
	contact_person varchar(255) NULL,
	description varchar(255) NULL,
	CONSTRAINT tb_vendor_pkey PRIMARY KEY (id),
	CONSTRAINT "FK_User_ID" FOREIGN KEY (user_id) REFERENCES tb_customer(id)
);


-- public.tb_vendor_legal_details definition

-- Drop table

-- DROP TABLE tb_vendor_legal_details;

CREATE TABLE tb_vendor_legal_details (
	id bigserial NOT NULL,
	gst_number varchar(255) NULL,
	pan_number varchar(255) NULL,
	vendor_id int8 NULL,
	reg_number varchar(255) NULL,
	gstnumber varchar(255) NULL,
	pannumber varchar(255) NULL,
	CONSTRAINT tb_vendor_legal_details_pkey PRIMARY KEY (id),
	CONSTRAINT uk_6sdj4mvk0by3vabkj24ydbxmk UNIQUE (vendor_id),
	CONSTRAINT fknkgd6bkoohpk8r9yd8bwapwph FOREIGN KEY (vendor_id) REFERENCES tb_vendor(id)
);


-- public.tb_catalog definition

-- Drop table

-- DROP TABLE tb_catalog;

CREATE TABLE tb_catalog (
	catalog_id bigserial NOT NULL,
	description varchar(255) NULL,
	"name" varchar(255) NULL,
	vendor_id int8 NULL,
	CONSTRAINT tb_catalog_pkey PRIMARY KEY (catalog_id),
	CONSTRAINT fk337hbsci2sl8t3uay6btnfs4i FOREIGN KEY (vendor_id) REFERENCES tb_vendor(id)
);


-- public.tb_category_vendor definition

-- Drop table

-- DROP TABLE tb_category_vendor;

CREATE TABLE tb_category_vendor (
	id bigserial NOT NULL,
	category_id int8 NOT NULL,
	vendor_id int8 NOT NULL,
	CONSTRAINT tb_category_vendor_pkey PRIMARY KEY (id),
	CONSTRAINT tb_category_vendor_unique UNIQUE (category_id, vendor_id),
	CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES tb_category(id),
	CONSTRAINT fk_vendor_id FOREIGN KEY (vendor_id) REFERENCES tb_vendor(id)
);


-- public.tb_product_vendor definition

-- Drop table

-- DROP TABLE tb_product_vendor;

CREATE TABLE tb_product_vendor (
	id bigserial NOT NULL,
	product_id int8 NOT NULL,
	vendor_id int8 NOT NULL,
	features jsonb NULL,
	CONSTRAINT tb_product_vendor_pkey PRIMARY KEY (id),
	CONSTRAINT fkdjfjuhnn2wg0abqd6xsrsuns9 FOREIGN KEY (vendor_id) REFERENCES tb_vendor(id),
	CONSTRAINT fkqw0x3r5ew1c79odh7vjm5chtq FOREIGN KEY (product_id) REFERENCES tb_product(id)
);


-- public.tb_sku definition

-- Drop table

-- DROP TABLE tb_sku;

CREATE TABLE tb_sku (
	id int8 NOT NULL,
	image_path varchar(255) NULL,
	"name" varchar(255) NOT NULL,
	weight varchar(255) NULL,
	sku_code varchar(255) NULL,
	created_date timestamp(6) NULL,
	last_modified_date timestamp(6) NULL,
	vendor_product_id int8 NOT NULL,
	created_by varchar(50) NULL,
	last_modified_by varchar(50) NULL,
	"type" public."sku_type" NULL,
	description varchar(255) NULL,
	stock int4 DEFAULT 100 NULL,
	cancel_eligible bool DEFAULT false NULL,
	return_eligible bool DEFAULT false NULL,
	subscription_eligible bool DEFAULT false NULL,
	is_active bool DEFAULT true NULL,
	dimension varchar(255) NULL,
	CONSTRAINT tb_sku_pkey PRIMARY KEY (id),
	CONSTRAINT tb_sku_unique UNIQUE (name, weight, vendor_product_id),
	CONSTRAINT fk_vendor_product_id FOREIGN KEY (vendor_product_id) REFERENCES tb_product_vendor(id)
);


-- public.tb_sku_price definition

-- Drop table

-- DROP TABLE tb_sku_price;

CREATE TABLE tb_sku_price (
	id bigserial NOT NULL,
	effective_date date NOT NULL,
	sale_price numeric(38, 2) NOT NULL,
	list_price numeric(38, 2) NOT NULL,
	created_by varchar(255) NULL,
	sku_id int8 NOT NULL,
	CONSTRAINT tb_sku_price_pkey PRIMARY KEY (id),
	CONSTRAINT tb_sku_price_unique UNIQUE (effective_date, sku_id),
	CONSTRAINT fkglyphaul1ltpi50mixkbq0jkq FOREIGN KEY (sku_id) REFERENCES tb_sku(id)
);
CREATE INDEX idx_sku_effective_date ON public.tb_sku_price USING btree (sku_id, effective_date);


-- public.tb_sku_sub_plan definition

-- Drop table

-- DROP TABLE tb_sku_sub_plan;

CREATE TABLE tb_sku_sub_plan (
	id bigserial NOT NULL,
	eligible_delivery_days jsonb NULL,
	sku_id int8 NOT NULL,
	sub_plan_id int8 NOT NULL,
	CONSTRAINT tb_sku_sub_plan_pkey PRIMARY KEY (id),
	CONSTRAINT tb_sku_sub_plan_unique UNIQUE (sku_id, sub_plan_id),
	CONSTRAINT fkg6o79d70ikvnemrx603stcek3 FOREIGN KEY (sub_plan_id) REFERENCES tb_sub_plan(id),
	CONSTRAINT fkhvllfkb32x0f6iogw2vh7c6qw FOREIGN KEY (sku_id) REFERENCES tb_sku(id)
);


-- public.tb_subscription definition

-- Drop table

-- DROP TABLE tb_subscription;

CREATE TABLE tb_subscription (
	id int8 NOT NULL,
	created_date timestamp(6) NOT NULL,
	delivery_address jsonb NULL,
	start_date date NULL,
	vendor_id int8 NULL,
	last_modified_date timestamp(6) NOT NULL,
	next_delivery_date date NULL,
	quantity int4 NOT NULL,
	status public."subscription_status" NULL,
	update_version int4 NULL,
	customer_id int8 NOT NULL,
	sku_id int8 NULL,
	created_by varchar(50) NULL,
	last_modified_by varchar(50) DEFAULT 'System'::character varying NULL,
	end_date date NULL,
	price_id int8 NOT NULL,
	subscription_type public."sku_type" NULL,
	delivery_mode public."delivery_mode_enum" NULL,
	subscription_plan_id int8 NULL,
	special_notes varchar(150) NULL,
	CONSTRAINT pk_sub_id PRIMARY KEY (id),
	CONSTRAINT unique_customer_vendor_sku UNIQUE (customer_id, sku_id),
	CONSTRAINT fk_customer_id FOREIGN KEY (customer_id) REFERENCES tb_customer(id) ON DELETE CASCADE,
	CONSTRAINT fk_sub_plan_id FOREIGN KEY (subscription_plan_id) REFERENCES tb_sub_plan(id)
);


-- public.tb_subscription_custom_days definition

-- Drop table

-- DROP TABLE tb_subscription_custom_days;

CREATE TABLE tb_subscription_custom_days (
	subscription_id int8 NOT NULL,
	custom_days int4 NULL,
	CONSTRAINT fkcpsbdmqw3qxkru6069775vqle FOREIGN KEY (subscription_id) REFERENCES tb_subscription(id) ON DELETE CASCADE ON UPDATE CASCADE
);


-- public.tb_cart_items definition

-- Drop table

-- DROP TABLE tb_cart_items;

CREATE TABLE tb_cart_items (
	id bigserial NOT NULL,
	discount float8 NOT NULL,
	quantity int4 NULL,
	unit_price float8 NOT NULL,
	cart_id int8 NULL,
	sku_id int8 NULL,
	CONSTRAINT tb_cart_items_pkey PRIMARY KEY (id),
	CONSTRAINT uk_lpxn9w9n52ifntburew6kw7vb UNIQUE (sku_id),
	CONSTRAINT fka7scce3udf66o87j267gfotlf FOREIGN KEY (cart_id) REFERENCES tb_cart(id),
	CONSTRAINT fknb9jt4yopaetwuq1u6jowdij2 FOREIGN KEY (sku_id) REFERENCES tb_sku(id)
);


-- public.tb_order definition

-- Drop table

-- DROP TABLE tb_order;

CREATE TABLE tb_order (
	id int8 DEFAULT nextval('tb_order_order_id_seq'::regclass) NOT NULL,
	central_tax numeric(38, 2) NULL,
	price numeric(38, 2) NULL,
	quantity int4 NOT NULL,
	state_tax numeric(38, 2) NULL,
	order_status public."order_status_enum" NULL,
	vendor_id int8 NULL,
	payment_id int8 NULL,
	subscription_id int8 NOT NULL,
	payment_status public."payment_status_enum" NULL,
	created_date timestamptz(6) NULL,
	last_modified_by varchar(50) NULL,
	last_modified_date timestamptz(6) NULL,
	created_by varchar(50) NULL,
	delivery_date date NULL,
	order_id bigserial NOT NULL,
	total_amount float8 NOT NULL,
	CONSTRAINT tb_order_order_status_check CHECK ((order_status = ANY (ARRAY['PENDING'::order_status_enum, 'SCHEDULED'::order_status_enum, 'IN_PROCESS'::order_status_enum, 'DELIVERED'::order_status_enum, 'CANCELED'::order_status_enum]))),
	CONSTRAINT tb_order_pkey PRIMARY KEY (id),
	CONSTRAINT uk_95ga12r14tprpgxyud1eaqf8j UNIQUE (payment_id),
	CONSTRAINT fkj016jvq9orxa9lvcaayec2d32 FOREIGN KEY (payment_id) REFERENCES tb_payment(payment_id),
	CONSTRAINT fkknj29d1k6k9yy4yseh4yfy8d6 FOREIGN KEY (subscription_id) REFERENCES tb_subscription(id) ON DELETE CASCADE
);


-- public.tb_order_items definition

-- Drop table

-- DROP TABLE tb_order_items;

CREATE TABLE tb_order_items (
	id bigserial NOT NULL,
	discount float8 NOT NULL,
	federal_tax float8 NOT NULL,
	quantity int4 NULL,
	state_tax float8 NOT NULL,
	unit_price float8 NOT NULL,
	order_id int8 NULL,
	sku_id int8 NULL,
	CONSTRAINT tb_order_items_pkey PRIMARY KEY (id),
	CONSTRAINT uk_o4d14cllhgal7bursg81v04o5 UNIQUE (sku_id),
	CONSTRAINT fk7udx0sta4987uw5sngxm9vh8g FOREIGN KEY (sku_id) REFERENCES tb_sku(id),
	CONSTRAINT fkm2jtpqpif3nvjyaap16os3xph FOREIGN KEY (order_id) REFERENCES tb_order(id)
);


-- public.tb_order_status_history definition

-- Drop table

-- DROP TABLE tb_order_status_history;

CREATE TABLE tb_order_status_history (
	id int8 NOT NULL,
	changed_at timestamp(6) NULL,
	changed_by varchar(255) NULL,
	new_status int2 NULL,
	old_status int2 NULL,
	order_id int8 NULL,
	CONSTRAINT tb_order_status_history_new_status_check CHECK (((new_status >= 0) AND (new_status <= 4))),
	CONSTRAINT tb_order_status_history_old_status_check CHECK (((old_status >= 0) AND (old_status <= 4))),
	CONSTRAINT tb_order_status_history_pkey PRIMARY KEY (id),
	CONSTRAINT fkeb3eu3lbqciruo0m1ilduuriw FOREIGN KEY (order_id) REFERENCES tb_order(id)
);


-- public.tb_service_attributes definition

-- Drop table

-- DROP TABLE tb_service_attributes;

CREATE TABLE tb_service_attributes (
	id int8 NOT NULL,
	no_of_uses int4 NOT NULL,
	valid_days int4 NULL,
	sku_id int8 NULL,
	CONSTRAINT tb_service_attributes_pkey PRIMARY KEY (id),
	CONSTRAINT uk_7wy1mm9y6tnxues1pia4bmyr1 UNIQUE (sku_id),
	CONSTRAINT fk8hl4glr097hfntilxjt8dy0o9 FOREIGN KEY (sku_id) REFERENCES tb_sku(id)
);



--Roles
INSERT INTO public.tb_role (role_id,role_name) VALUES
	 (101,'ADMIN'),
	 (102,'USER'),
	 (103,'VENDOR'),
	 (104,'CUSTOMER_CARE');

--Test Vendor Creation
INSERT INTO public.tb_customer (id,created_date,delivery_address,email,email_verified,first_name,last_name,mobile,mobile_verified,reg_device,reg_source,is_active,created_by,last_modified_date,last_modified_by,delivery_instructions,mobile_verified_timestamp,date_of_birth,"gender",preferences,country_code,"type") VALUES
	 (1000,'2024-11-25 09:14:43.576353','{"city": "Hyderabad", "state": "Telangana", "country": "India", "zipCode": "500001", "address1": "Syndicate Colony", "address2": "8-2-1"}',NULL,false,'Shankar',NULL,9959696478,true,NULL,'android',true,NULL,'2024-11-25 09:14:43.576353',NULL,'null','2024-11-25 09:16:09.860145',NULL,NULL,'null','+91',NULL),
	 (1001,'2024-11-24 14:04:19.177406','{"city": "Secunderabad", "state": "Telangana", "country": "India", "zipCode": "518521", "address1": "Sai Colony", "address2": "Ameeret"}'
	 ,NULL,false,'RamanaSingh',NULL,9090909090,true,NULL,'android',true,NULL,'2024-11-24 14:04:19.177406',NULL,'null','2024-11-24 14:04:44.17285',NULL,NULL,
	 'null','+91',NULL),
	(1002,'2024-12-06 11:33:07.257302','null',NULL,false,NULL,NULL,9912149081,false,NULL,'android',true,NULL,'2024-12-06 11:33:07.257302',NULL,'null',NULL,NULL,NULL,'null','+91','USER'::public."user_type");

	INSERT INTO public.tb_user_roles (user_id,role_id) VALUES
    	 (1000,103),
    	 (1001,101),
    	 (1002,102);

INSERT INTO public.tb_vendor (id,business_address,business_name,contact_number,created_date,communication_email,last_modified_date,owner_name,service_area,
user_id,status,last_modified_by,created_by,approval_status,banner_image,business_type,contact_person,description) VALUES
	 (91,'{"city": "3", "state": "3", "country": "3", "addrees1": "1", "address2": "2"}','H2A2 Farms',
	 '9090909090','2024-12-20 15:02:46.037657-05','Hafeez@gmail.com','2024-12-20 15:02:46.037657-05','Hafeez','{"areas": {},
	  "additionalProp2": {}, "additionalProp3": {}}',1001,'ACTIVE'::public."vendor_status_enum",NULL,NULL,'APPROVED'::public."verification_status_enum",
	  'string','Farming','string','string');

INSERT INTO public.tb_category (id,description,"name",image_path,"type") VALUES
	 (103,'Farm Stay','Farm Stay',NULL,'Agriculture'),
	 (102,'Vegetables and Leafs','Vegetables','image','Agriculture'),
	 (105,'Pulses','Pulses','image','Agriculture'),
	 (106,'Oils','Oils','image','Agriculture'),
	 (107,'Personal Care','Personal Care','image','Health & Wellness'),
	 (108,'Nutritional Products','Nutritional Products','image','Health & Wellness'),
	 (109,'Subscriptions','Subscriptions','image','Services'),
	 (110,'Automotive','Automotive','image','Services'),
	 (111,'Poultry & Eggs','Poultry & Eggs','image','Agriculture'),
	 (112,'Meat','Meat','image','Agriculture');
INSERT INTO public.tb_category (id,description,"name",image_path,"type") VALUES
	 (113,'Packaged Meat','Packaged Meat','image','Food & Beverage'),
	 (114,'Ready-to-Cook','Ready-to-Cook','image','Food & Beverage'),
	 (104,'Milk and Its products','Dairy',NULL,'Dairy');

INSERT INTO public.tb_category_vendor (category_id,vendor_id) VALUES
	 (102,91),
	 (103,91),
	 (111,91),
	 (112,91);
INSERT INTO public.tb_product (id,description,image_path,"name",category_id,created_date,last_modified_date,created_by,last_modified_by) VALUES
	 (1003,'Farm Stay',NULL,'Farm Stay',103,NULL,NULL,NULL,NULL),
	 (1004,'Milk',NULL,'Milk',104,NULL,NULL,NULL,NULL),
	 (1002,'Tamato',NULL,'Tamato',102,NULL,NULL,NULL,NULL),
	 (1005,'Potatoes',NULL,'Potatoes',102,NULL,NULL,NULL,NULL),
	 (1006,'Meal Plans',NULL,'Meal Plans',109,NULL,NULL,NULL,NULL),
	 (1007,'Farm-fresh country chicken, free from antibiotics and hormones, available whole or in premium cuts.',NULL,'Country Chicken',111,NULL,NULL,NULL,NULL),
	 (1008,'Freshly laid eggs, organic from free-range hens or conventionally farmed, rich in nutrients.',NULL,'Country Eggs',111,NULL,NULL,NULL,NULL),
	 (1009,'High-quality meat sourced from well-raised goats and lambs, perfect for curries or grilling.',NULL,'Goat Meat',112,NULL,NULL,NULL,NULL),
	 (1010,'Sustainably farmed fish, available in freshwater or seawater varieties, perfect for a healthy diet.',NULL,'Fish',112,NULL,NULL,NULL,NULL);
INSERT INTO public.tb_product_vendor (id,product_id,vendor_id,features) VALUES
	 (10003,1003,91,'{"feature_1": "Rich in vitamin-C"}'),
	 (5,1004,91,'{"Variants Available": "Cow, Buffalo and Skim"}'),
	 (6,1005,91,'{"Variants Available": "Cow, Buffalo and Skim"}'),
	 (10002,1002,91,'{"feature_1": "Rich in vitamin-C"}'),
	 (7,1007,91,'{"Feature": "Farm-fresh country chicken, free from antibiotics and hormones, available whole or in premium cuts."}'),
	 (8,1008,91,'{"Feature": "Freshly laid eggs, organic from free-range hens or conventionally farmed, rich in nutrients."}'),
	 (9,1010,91,'{}');

INSERT INTO public.tb_sub_plan (id,description,"name",frequency,delivery_mode,subscription_eligible) VALUES
	 (1,'Order any time but delivery is on FIXED days only.','One Time Delivery','ONE_TIME'::public."subscription_frequency",'FIXED'::public."delivery_mode_enum",false),
	 (4,'Order any time but delivery is on customer selected day','One time ','ONE_TIME'::public."subscription_frequency",'FLEXIBLE'::public."delivery_mode_enum",true),
	 (5,'Weekly recurring order , delivered on specific day.Example every sunday.','Weekly Recurring Fixed Delivery','WEEKLY'::public."subscription_frequency",'FIXED'::public."delivery_mode_enum",true),
	 (6,'Monthly recurring order , delivered on specific day.Example every month 1st.','Monthly Recurring Fixed Delivery','MONTHLY'::public."subscription_frequency",'FIXED'::public."delivery_mode_enum",true),
	 (2,'Recurring order with custome delivered dates.','Customer Days','CUSTOM'::public."subscription_frequency",'FLEXIBLE'::public."delivery_mode_enum",true),
	 (3,'Recurring order with daily delivery.','Daily Recurring','DAILY'::public."subscription_frequency",'FLEXIBLE'::public."delivery_mode_enum",true);

INSERT INTO public.tb_sku (id,image_path,"name",weight,sku_code,created_date,last_modified_date,vendor_product_id,created_by,last_modified_by,"type",description,stock,cancel_eligible,return_eligible,subscription_eligible,is_active,dimension) VALUES
	 (202,'http://example.com/fruits/orange_1.png','Whole Country Chicken-Organic','1 Kg','SKU_7_OGANIC_CHICKEN','2024-12-31 05:51:59.484766','2024-12-31 05:51:59.484766',7,NULL,NULL,'ITEM'::public."sku_type",'Organic and Own farm grown vegetables',100,true,true,true,true,'10*12*8'),
	 (502,'http://example.com/fruits/orange_1.png','Farm Stay','1 Person','SKU_10003_Farm Stay','2024-12-31 06:37:21.754143','2024-12-31 06:37:21.754143',10003,NULL,NULL,'SERVICE'::public."sku_type",'Enjoy the thrill and pleasure of surfing under different winds and with large varieties.',100,true,true,false,true,NULL),
	 (652,'http://example.com/fruits/orange_1.png','A2 Buffalo Milk Full Cream','1 Liter','SKU_5_A2 Buffalo Milk Double Toned','2024-12-31 23:41:11.066202','2024-12-31 23:41:11.066202',5,NULL,NULL,'ITEM'::public."sku_type",'A2 Buffalo Milk Double Toned',100,false,false,true,true,'10*12*8'),
	 (653,'http://example.com/fruits/orange_1.png','Fresh Water Fish-Korra Meenu','1 LKg','SKU_9_FISH_1','2024-12-31 23:41:11.066','2024-12-31 23:41:11.066202',9,NULL,NULL,'ITEM'::public."sku_type",'A2 Buffalo Milk Double Toned',100,false,false,true,true,'10*12*8'),
	 (654,'http://example.com/fruits/orange_1.png','Fresh Water Fish-Korra Meenu','2 LKg','SKU_9_FISH_2','2024-12-31 23:41:11.066','2024-12-31 23:41:11.066202',9,NULL,NULL,'ITEM'::public."sku_type",'A2 Buffalo Milk Double Toned',100,false,false,true,true,'10*12*8');
INSERT INTO public.tb_sku_price (id,effective_date,sale_price,list_price,created_by,sku_id) VALUES
	 (8,'2024-12-30',90.00,100.00,NULL,202),
	 (16,'2024-12-30',1999.00,2599.00,NULL,502),
	 (17,'2024-12-31',90.00,100.00,NULL,652),
	 (18,'2024-02-01',100.00,110.00,NULL,652),
	 (19,'2025-01-01',599.00,399.00,NULL,653),
	 (20,'2025-01-01',299.00,199.00,NULL,654);
INSERT INTO public.tb_sku_sub_plan (id,eligible_delivery_days,sku_id,sub_plan_id) VALUES
	 (9,'{"delivery_days": ["SUNDAY", "WEDNESDAY"]}',202,1),
	 (15,'{"delivery_days": ["ANYDAY"]}',652,3),
	 (16,'{"delivery_days": ["ANYDAY"]}',652,2),
	 (20,'{"delivery_days": ["ANYDAY"]}',652,4),
	 (17,'{"delivery_days": ["ANYDAY"]}',502,4),
	 (18,'{"delivery_days": ["SUNDAY"]}',653,1),
	 (19,'{"delivery_days": ["SUNDAY"]}',654,1);

INSERT INTO public.tb_service_attributes (id,no_of_uses,valid_days,sku_id) VALUES
	 (1,1,30,502);



