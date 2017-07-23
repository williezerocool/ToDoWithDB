create database ToDo;

use ToDo;

create table Task (
    `Name` varchar(256) not null,
    CreateDate timestamp default current_timestamp,
    IsComplete bit(1) not null default 0
);