
CREATE TYPE order_status_enum AS ENUM ('CREATED', 'IN_PROCESS','SCHEDULED','DELIVERED','CANCELED');
CREATE TYPE ship_type_enum AS ENUM ('INSTORE_PICKUP', 'HOME_DELIVERY');
CREATE TYPE payment_type_enum AS ENUM ('CASH_ON_DELIVERY','ONLINE', 'IN_STORE_PAYMENT');
CREATE TYPE Subscription_Status AS ENUM ('ACTIVE', 'PAUSED', 'CANCELLED');
CREATE TYPE Subscription_Item_Status AS ENUM ('PENDING', 'IN_PROGRESS', 'DELIVERED','PAUSED','CANCELLED');
	
INSERT INTO public.roles (role_id, role_name) VALUES(101,'ADMIN');
INSERT INTO public.roles (role_id, role_name) VALUES(102,'USER');
INSERT INTO public.roles (role_id, role_name) VALUES(103,'STORE');




create type PaymentStatus as enum ('DUE','PAID')
create cast (varchar as PaymentStatus) with inout as implicit
create cast (PaymentStatus as varchar) with inout as implicit
create type OrderStatus as enum ('PENDING','SCHEDULED','IN_PROCESS','DELIVERED','CANCELED')
create cast (varchar as OrderStatus) with inout as implicit
create cast (OrderStatus as varchar) with inout as implicit
create type PaymentType as enum ('CASH_ON_DELIVERY','ONLINE','IN_STORE_PAYMENT')
create cast (varchar as PaymentType) with inout as implicit
create cast (PaymentType as varchar) with inout as implicit
create type SubscriptionFrequency as enum ('ONE_TIME','DAILY','ALTERNATE_DAY','WEEKLY','CUSTOM')
create cast (varchar as SubscriptionFrequency) with inout as implicit
create cast (SubscriptionFrequency as varchar) with inout as implicit
create type SubscriptionStatus as enum ('ACTIVE','PAUSED','CANCELLED','EXPIRED')
create cast (varchar as SubscriptionStatus) with inout as implicit
create cast (SubscriptionStatus as varchar) with inout as implicit
create type VendorStatus as enum ('NEW','ACTIVE','DELETED','SUSPENDED')
create cast (varchar as VendorStatus) with inout as implicit
create cast (VendorStatus as varchar) with inout as implicit
create type VerificationStatus as enum ('Pending','Approved','Rejected')
create cast (varchar as VerificationStatus) with inout as implicit
create cast (VerificationStatus as varchar) with inout as implicit
alter table if exists tb_vendor add column verification_status VerificationStatus