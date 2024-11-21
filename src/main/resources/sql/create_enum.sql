CREATE TYPE public."subscription_status_v2" AS ENUM (
	'ACTIVE',
	'PAUSED',
	'CANCELLED',
	'EXPIRED',
	'DELETED',
	'PENDING');

ALTER TABLE tb_subscription
  ALTER COLUMN status TYPE public."subscription_status_v2"
  USING status::text::public."subscription_status_v2";

 DROP TYPE public."subscription_status";

ALTER TYPE public."subscription_status_v2" RENAME TO "subscription_status";