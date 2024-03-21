create table cars(
id bigint primary key generated always as identity,
mark varchar(30) unique not null,
model varchar(40) unique not null,
price bigint not null check (price > 0)
);

create table persons(
id bigint primary key generated always as identity,
name varchar(30) not null,
age INTEGER not null,
drivers_license boolean check (age>18) SET DEFAULT false,
car_id bigint
foreign key (car_id) references cars (id)
);