-- Wrapper init script: desactivar comprobaciones FK, ejecutar el SQL del proyecto y volver a activarlas
SET FOREIGN_KEY_CHECKS=0;

-- BEGIN Database.sql content

create database project3;
use project3;

#1
create table RestaurantManagement(
Restaurant_id numeric (10) primary key,
Restaurant_name varchar (30),
Restaurant_address varchar (30),
Manager_id numeric (10) references Manager (Manager_id)
);

#2
create table Manager(
Manager_id numeric (10) primary key,
Manager_Fname varchar (30),
Manager_Mname varchar (30),
Manager_Lname varchar (30),
Manager_number numeric(10),
Manager_salary numeric (5)
);

#3
create table Item(
Item_food varchar (30),
food_price numeric(4),
food_id numeric (5),#PK
Item_appetizers varchar (30),
appetizers_price numeric(4),
appetizers_id numeric (5),#PK
Item_drinks varchar (30),
drinks_price numeric(4),
drinks_id numeric (5),#PK
Receipt_id numeric (10) references Receipt (Receipt_id),
primary key (food_id, appetizers_id, drinks_id)
);

#4
create table Chef(
Chef_id numeric (10) primary key,
Chef_name varchar (30),
Chef_manager varchar (30),
Chef_salary numeric (5),
Manager_id numeric (10) references Manager (Manager_id)
);

#5
create table Cashier(
Cashier_id numeric (10) primary key,
Cashier_name varchar (30),
Cashier_salary numeric (5)
);

#6
create table Receipt(
Receipt_id numeric (10) primary key,
Receipt_time time,
Receipt_date date,
Receipt_total numeric (10)
);

#7
create table Dependent_Manager(
Dependent_name varchar (30) primary key,
Dependent_relation varchar (30),
Dependent_sex enum ('M','F'),# specify M or F only
Manager_id numeric (10) references Manager (Manager_id)
);

#8
create table Dependent_chef(
Dependent_name varchar (30) primary key,
Dependent_relation varchar (30),
Dependent_sex enum ('M','F'),# specify M or F only
Chef_id  numeric (10) references Chef (Chef_id )
);

#9
create table Dependent_Cashier(
Dependent_name varchar (30) primary key,
Dependent_relation varchar (30),
Dependent_sex enum ('M','F'),# specify M or F only
Cashier_id numeric (10) references Cashier (Cashier_id  )
);

#10
create table Restaurant_address(
Restaurant_address varchar (30)primary key,
Restaurant_id numeric (10) references Restaurant (Restaurant_id)
);

#11
create table Chef_Prepare_item(
Chef_id numeric (10) references Chef (Chef_id),
food_id numeric (5) references Item (food_id),
sweet_id numeric (5) references Item (sweet_id),
drinks_id numeric (5) references Item (drinks_id)
);

#12
create table Receipt_takenBy_Cashier(
Receipt_id numeric (10) references Receipt (Receipt_id),
Cashier_id numeric (10) references Cashier (Cashier_id)
);

insert into RestaurantManagement (Restaurant_id ,Restaurant_name ,Restaurant_address,Manager_id)values(56471,'project_resturant','khobar',3214);

insert into Manager (Manager_id,Manager_Fname,Manager_Mname ,Manager_Lname ,Manager_number ,Manager_salary ) values (3214,'ahmed','khalid','alfahad',053276488,7000);

#Chef1
insert into Chef (Chef_id ,Chef_name ,Chef_manager ,Chef_salary ,Manager_id ) values(2561,'abduallah','ahmed',6000,3214);

#Chef2
insert into Chef (Chef_id ,Chef_name ,Chef_manager ,Chef_salary ,Manager_id ) values(2562,'saleh','ahmed',6000,3214);

#Chef3
insert into Chef (Chef_id ,Chef_name ,Chef_manager ,Chef_salary ,Manager_id ) values(2563,'mohammad','ahmed',6000,3214);

#Chef4
insert into Chef (Chef_id ,Chef_name ,Chef_manager ,Chef_salary ,Manager_id ) values(2564,'khalid','ahmed',6000,3214);



#Cashier1
insert into Cashier (Cashier_id ,Cashier_name ,Cashier_salary )values (4231,'abdualmajeed',7000);

#Cashier2
insert into Cashier (Cashier_id ,Cashier_name ,Cashier_salary )values (4232,'abdualrahman',7000);

insert into Receipt (Receipt_id ,Receipt_time ,Receipt_date ,Receipt_total ) values (1,'12:45:56','2022-01-23',300);

insert into Receipt (Receipt_id ,Receipt_time ,Receipt_date ,Receipt_total ) values (2,'12:44:32','2022-01-23',200);

insert into Receipt (Receipt_id ,Receipt_time ,Receipt_date ,Receipt_total ) values (3,'1:30:50','2022-01-24',300);
insert into Receipt (Receipt_id ,Receipt_time ,Receipt_date ,Receipt_total ) values (4,'3:30:55','2022-01-24',360);
insert into Receipt (Receipt_id ,Receipt_time ,Receipt_date ,Receipt_total ) values (5,'3:00:50','2022-01-25',400);
insert into Receipt (Receipt_id ,Receipt_time ,Receipt_date ,Receipt_total ) values (6,'2:00:00','2022-01-25',200);
insert into Receipt (Receipt_id ,Receipt_time ,Receipt_date ,Receipt_total ) values (7,'3:00:00','2022-01-26',150);

insert into Dependent_Manager (Dependent_name ,Dependent_relation ,Dependent_sex ,Manager_id ) values ('maram','wife','f',3214);
insert into Dependent_Manager (Dependent_name ,Dependent_relation ,Dependent_sex ,Manager_id ) values ('marwa','child','f',3214);


insert into Dependent_chef (Dependent_name ,Dependent_relation ,Dependent_sex ,Chef_id  ) values ('saad','son','m',2562);
insert into Dependent_chef (Dependent_name ,Dependent_relation ,Dependent_sex ,Chef_id  ) values ('sara','wife','f',2563);
insert into Dependent_chef (Dependent_name ,Dependent_relation ,Dependent_sex ,Chef_id  ) values ('norah','wife','f',2561);


insert into Dependent_Cashier (Dependent_name ,Dependent_relation ,Dependent_sex ,Cashier_id  ) values ('sara','daughter','f',4231);
insert into Dependent_Cashier (Dependent_name ,Dependent_relation ,Dependent_sex ,Cashier_id  ) values ('lama','daughter','f',4232);



insert into Item (Item_food ,food_price ,food_id ,Item_appetizers ,appetizers_price ,appetizers_id ,Item_drinks ,drinks_price ,drinks_id ,Receipt_id ) values ('buratta pizza',56.0,1,'Dynamite shrimp',39.0,1,'cola',5.0,1,1);
insert into Item (Item_food ,food_price ,food_id ,Item_appetizers ,appetizers_price ,appetizers_id ,Item_drinks ,drinks_price ,drinks_id ,Receipt_id ) values ('pink pasta',37.0,2,'mac&cheese balls',45.0,2,'7up',5.0,2,2);
insert into Item (Item_food ,food_price ,food_id ,Item_appetizers ,appetizers_price ,appetizers_id ,Item_drinks ,drinks_price ,drinks_id ,Receipt_id ) values ('spaghetti',40.0,3,'tiramisu',42.0,3,'orang juice',15.0,3,3);
insert into Item (Item_food ,food_price ,food_id ,Item_appetizers ,appetizers_price ,appetizers_id ,Item_drinks ,drinks_price ,drinks_id ,Receipt_id ) values ('rosemary salmon',87.0,4,'molten chocolate',19.0,4,'mojito',25.0,4,4);



insert into Chef_Prepare_item (Chef_id ,food_id ,sweet_id ,drinks_id ) values (2561,1,1,1);
insert into Chef_Prepare_item (Chef_id ,food_id ,sweet_id ,drinks_id ) values (2562,2,2,2);
insert into Chef_Prepare_item (Chef_id ,food_id ,sweet_id ,drinks_id ) values (2563,3,3,3);
insert into Chef_Prepare_item (Chef_id ,food_id ,sweet_id ,drinks_id ) values (2564,4,4,4);


insert into Receipt_takenBy_Cashier (Receipt_id ,Cashier_id )values(1,1);
insert into Receipt_takenBy_Cashier (Receipt_id ,Cashier_id )values (2,1);
insert into Receipt_takenBy_Cashier (Receipt_id ,Cashier_id )values (3,1);
insert into Receipt_takenBy_Cashier (Receipt_id ,Cashier_id )values (4,1);
insert into Receipt_takenBy_Cashier (Receipt_id ,Cashier_id )values (5,2);
insert into Receipt_takenBy_Cashier (Receipt_id ,Cashier_id )values (6,2);
insert into Receipt_takenBy_Cashier (Receipt_id ,Cashier_id )values (7,2);


#Union
select Cashier_name from Cashier union select Dependent_name from Dependent_Cashier;

#Intersect error
SELECT DISTINCT Manager_salary FROM Manager INNER JOIN Cashier USING (Cashier_salary);

#DISTINCT
SELECT DISTINCT Receipt_total FROM Receipt;

#aggregate
SELECT AVG(Receipt_total) average_Receipt_total_price FROM Receipt;
SELECT max(Receipt_total) average_Receipt_total_price FROM Receipt;
SELECT min(Receipt_total) average_Receipt_total_price FROM Receipt;

#not
SELECT * FROM Chef WHERE NOT Chef_name='khalid';

#like
SELECT * FROM Chef WHERE Chef_name LIKE 'k%';

#ORDER BY
SELECT * FROM Item ORDER BY Item_food DESC;
SELECT * FROM Item ORDER BY Item_food ASC;


#GROUP BY,HAVING,count null
SELECT COUNT(Chef_id ),Chef_name FROM Chef GROUP BY Chef_name HAVING COUNT(Chef_id ) > 2561;

#in
SELECT * FROM Receipt WHERE Receipt_date IN ('2022-01-24', '2022-01-25');

#BETWEEN
SELECT * FROM Receipt WHERE Receipt_total BETWEEN 300 AND 400;

#GROUP BY
SELECT COUNT(Chef_id),Chef_name FROM Chef GROUP BY Chef_name;

#having
SELECT COUNT(Receipt_id), Receipt_total FROM Receipt GROUP BY Receipt_total HAVING COUNT(Receipt_id) < 5;

#case
insert into Receipt (Receipt_id ,Receipt_time ,Receipt_date,Receipt_total ) values (10,'13:46:51','2022-02-23',650);
select Receipt_id ,Receipt_time ,Receipt_date ,Receipt_total,case when Receipt_total between 600 and 700 then 'salary increasing' else 'normal'end as 'salary' from Receipt;


#insert
insert into Receipt (Receipt_id ,Receipt_time ,Receipt_date,Receipt_total ) values (8,'12:45:51','2022-01-23',300);

create table Receipt_copy (
Receipt_id numeric (10) primary key,
Receipt_time time,
Receipt_date date,
Receipt_total numeric (10)
);

insert into Receipt_copy (Receipt_id ,Receipt_time ,Receipt_date ,Receipt_total ) values (1,'12:45:51','2022-01-23',300);

DELETE FROM Receipt_copy WHERE Receipt_id=1;

#index
create index Itemindex on Item (Item_food);

#assertion
create assertion salary_constraint check ( not exists ( select * from cashier ,Manager where Cashier_salary > Manager_salary));

#procedure
 delimiter $$
 create procedure Chefname (in Cheffname varchar (10))
 begin
 select *
 from chef
 where Chef_name = Cheffname;
 end $$
 delimiter ;

call Chefname('mohammad');
call Chefname('abduallah');

# trigger
Create trigger uppercase before insert on manager
for each row
Set NEW.Manager_Fname = UPPER(NEW.Manager_Fname);

insert into Manager (Manager_id,Manager_Fname,Manager_Mname ,Manager_Lname ,Manager_number ,Manager_salary ) values (3217,'Yazeed','fahad','alahmad',053276488,7000);

Select * from Manager ;


CREATE TABLE `appetizers` (
  `id` int(11) NOT NULL,
  `name` varchar(250) NOT NULL,
  `price` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


INSERT INTO `appetizers` (`id`, `name`, `price`) VALUES
(1, 'Truffel Fries', 23),
(2, 'Molten Chocolate', 12),
(3, 'Mac&Cheese Balls', 12),
(4, 'Dynamite Shrimp', 32),
(5, 'Kheera', 10);

CREATE TABLE `drinks` (
  `id` int(11) NOT NULL,
  `name` varchar(250) NOT NULL,
  `price` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


INSERT INTO `drinks` (`id`, `name`, `price`) VALUES
(1, 'cola', 6),
(2, '7up', 6),
(3, 'orange juice', 10),
(4, 'mojito', 14),
(5, 'Red Bull', 8);

CREATE TABLE `maincourse` (
  `id` int(11) NOT NULL,
  `name` varchar(250) NOT NULL,
  `price` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


INSERT INTO `maincourse` (`id`, `name`, `price`) VALUES
(1, 'Buratta Pizza', 54),
(2, 'Pink Pasta', 12),
(3, 'Rosemary Salmon', 30),
(4, 'Spaghetti', 8),
(5, 'Crown Pizza', 50);


ALTER TABLE `appetizers`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `drinks`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `maincourse`
  ADD PRIMARY KEY (`id`);


ALTER TABLE `appetizers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;


ALTER TABLE `drinks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;


ALTER TABLE `maincourse`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

-- END Database.sql content

SET FOREIGN_KEY_CHECKS=1;

