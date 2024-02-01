create schema if not exists ticketlist;

create table if not exists doctors
(
    id         bigserial primary key,
    name       varchar(255) not null,
    roomNumber varchar(255) not null
);

create table if not exists tickets
(
    id         bigserial primary key,
    start_time timestamp    not null,
    end_time   timestamp    not null,
    status     varchar(255) not null
);

create table if not exists patients
(
    id       bigserial primary key,
    name     varchar(255) not null,
    birthday timestamp    not null
);

create table if not exists doctors_tickets
(
    doctor_id  bigint not null,
    ticket_id  bigint not null,
    patient_id bigint null,
    primary key (doctor_id, ticket_id),
    constraint fk_doctors_tickets_doctors foreign key (doctor_id) references doctors (id) on delete cascade on update no action,
    constraint fk_doctors_tickets_tickets foreign key (ticket_id) references tickets (id) on delete cascade on update no action,
    constraint fk_patients_tickets_patients foreign key (patient_id) references patients (id) on delete cascade on update no action,
    constraint fk_patients_tickets_tickets foreign key (ticket_id) references tickets (id) on delete cascade on update no action
);
