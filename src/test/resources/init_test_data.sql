INSERT INTO public.tb_category (description,name,image_path) VALUES
	 ('Automobile Services','Automobile Services',NULL),
	 ('Milk and Milk products','Dairy Products',NULL),
	 ('Vegetables and Leafs','Vegetables',NULL),
	 ('Ayurvedic Medicines','Ayurvedic Medicines',NULL);


INSERT INTO public.tb_product (id,description,features,image_path,name,category_id,created_date,last_modified_date,created_by,last_modified_by) VALUES
	 (1000,'Car Wash',NULL,NULL,'Car Wash',100,NULL,NULL,NULL,NULL),
	 (1002,'Tomato',NULL,NULL,'Tomato',101,NULL,NULL,NULL,NULL),
	 (1001,'Milk',NULL,NULL,'Milk Delivery',101,NULL,NULL,NULL,NULL),
	 (1003,'Knee Pain Medicines',NULL,NULL,'Knee Pain Medicines',103,NULL,NULL,NULL,NULL);

INSERT INTO public.tb_vendor (business_address,business_name,contact_number,created_date,communication_email,last_modified_date,owner_name,service_area,user_id,status,last_modified_by,created_by,approval_status,banner_image,business_type,contact_person,description) VALUES
	 ('{"state": "Telangana", "mandal": "Bhoompally", "distict": "Siddipet", "village": "Habsipur", "zipcode": "502108", "latitude": "", "longtidu": ""}','Sri Balaji Servicing Centre And Water Wash','919000067890','2024-11-06 17:34:45.381323-05','sri_balalji_service@gmail.com',NULL,'Ramesh K','{"areas": ["Mirdoddi", "Boompally", "502108", "502103"]}',NULL,'ACTIVE'::public.vendor_status_enum,NULL,NULL,'PENDING'::public.verification_status_enum,NULL,NULL,NULL,NULL),
	 ('{"state": "Karnataka", "mandal": "Hebbal", "distict": "Hebbal", "village": "Hebbal", "zipcode": "900001", "latitude": "", "longtidu": ""}','Sri Ayurvedic Treatments','919912149048','2024-10-05 00:47:33.275692-04','sri_ayurvedic@gmail.com',NULL,'Amarnath Reddy','{"areas": ["Siddipet", "Dubbak", "502108", "502103", "500011", "india"]}',NULL,'ACTIVE'::public.vendor_status_enum,NULL,NULL,'APPROVED'::public.verification_status_enum,NULL,NULL,NULL,NULL),
	 ('{"state": "Telangana", "mandal": "Mirdoddi", "distict": "Siddipet", "village": "Kasulabad", "zipcode": "502108", "latitude": "", "longtidu": ""}','Mirdoddi Farm Fresh Daily & Veggies','919912149049','2024-10-05 00:47:33.275692-04','mirdoddi_farms@gmail.com',NULL,'Swamy Kunta','{"areas": ["Siddipet", "Dubbak", "502108", "502103"]}',NULL,'ACTIVE'::public.vendor_status_enum,NULL,NULL,'APPROVED'::public.verification_status_enum,NULL,NULL,NULL,NULL);

INSERT INTO public.tb_sku (id,image_path,name,"size",sku_code,created_date,last_modified_date,product_id,created_by,last_modified_by,"type",description,effective_date,processing_fee,service_valid_days,shipping_price,stock,vendor_id,cancel_eligible,return_eligible,subscription_eligible,available) VALUES
	 (10003,NULL,'Organic Tomato-2KG','2 KG','Tomato',NULL,NULL,1002,NULL,NULL,'ITEM'::public.sku_type,NULL,'2024-11-11',NULL,NULL,0.00,NULL,1,true,true,true,true),
	 (10004,NULL,'Buffalo Full Milk','2 LTR','MILK_2LTR',NULL,NULL,1001,NULL,NULL,'ITEM'::public.sku_type,NULL,'2024-10-11',NULL,NULL,0.00,NULL,1,true,true,true,true),
	 (10005,NULL,'Maharishi Ayurveda 100%Natural Healing Pain Relief Knee Joints ','100Gms','AYUR_1',NULL,NULL,1003,NULL,NULL,'ITEM'::public.sku_type,NULL,'2024-10-11',NULL,NULL,0.00,NULL,3,true,true,true,true),
	 (10001,NULL,'Monthly Car Wash','1 Service','CAR_WASH_1',NULL,NULL,1000,NULL,NULL,'SERVICE'::public.sku_type,NULL,NULL,NULL,30,NULL,NULL,2,true,true,true,true);

INSERT INTO public.tb_sku_price (effective_date,sale_price,list_price,created_by,sku_id) VALUES
	 ('2024-11-15',90.00,110.00,'',10004),
	 ('2024-11-16',90.00,110.00,'',10004),
	 ('2024-11-16',75.00,90.00,'',10003),
	 ('2025-01-01',135.00,150.00,'',10004),
	 ('2024-11-21',2000.00,2300.00,'',10005),
	 ('2025-01-01',2300.00,2800.00,NULL,10005),
	 ('2024-11-20',110.00,120.00,'',10004),
	 ('2024-11-20',250.00,250.00,NULL,10001);

INSERT INTO public.tb_sub_plan (description,name,frequency) VALUES
	 ('One Time Delivery','One Time Delivery','ONE_TIME'::public.subscription_frequency),
	 ('Custom Days Delivery Options','Customer Days','CUSTOM'::public.subscription_frequency),
	 ('Recurring Orders','Daily Delivery','DAILY'::public.subscription_frequency);

INSERT INTO public.tb_sku_sub_plan (eligible_delivery_days,sku_id,sub_plan_id) VALUES
	 ('{"delivery_mode": "FIXED", "delivery_days": ["WEDNESDAY", "SUNDAY"]}',10003,1),
	 ('{"delivery_mode": "FLEXIBLE"}',10004,1),
	 ('{}',10004,2),
	 ('{"delivery_mode": "FLEXIBLE"}',10005,1),
	 ('{"delivery_mode": "FLEXIBLE"}',10001,1);


