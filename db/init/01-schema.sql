create table if not exists users
(
    user_id       int auto_increment
        primary key,
    name          varchar(100)                   not null,
    email         varchar(100)                   not null,
    address       varchar(50) default 'homeless' null,
    password      varchar(50) default '0'        null,
    password_hash varchar(72)                    null,
    constraint uq_users_email
        unique (email)
);

create table if not exists Customer
(
    customer_id  int auto_increment
        primary key,
    user_id      int          not null,
    company_name varchar(100) not null,
    email        varchar(100) null,
    index ix_customer_user_id (user_id),
    constraint fk_customer_user
        foreign key (user_id) references users (user_id)
            on update cascade
);

create table if not exists Invoice
(
    invoice_id  int auto_increment
        primary key,
    customer_id int                                   not null,
    amount      decimal(10, 2)                        not null,
    due_date    date                                  not null,
    status      varchar(20) default 'open'            not null,
    created_at  timestamp   default CURRENT_TIMESTAMP null,
    index ix_invoice_customer_id (customer_id),
    constraint fk_invoice_customer
        foreign key (customer_id) references Customer (customer_id)
            on update cascade
);

create table if not exists Vendor
(
    vendor_id    int auto_increment
        primary key,
    user_id      int          not null,
    company_name varchar(100) null,
    index vendor_ibfk_1_idx (user_id),
    constraint vendor_ibfk_1
        foreign key (user_id) references users (user_id)
);

create table if not exists Bill
(
    bill_id    int auto_increment
        primary key,
    vendor_id  int                                   not null,
    amount     decimal(10, 2)                        null,
    due_date   date                                  null,
    status     varchar(20) default 'unpaid'          null,
    created_at timestamp   default CURRENT_TIMESTAMP null,
    index bill_ibfk_1_idx (vendor_id),
    constraint bill_ibfk_1
        foreign key (vendor_id) references Vendor (vendor_id)
);

create table if not exists Payment
(
    payment_id      int auto_increment
        primary key,
    bill_id         int                                 null,
    invoice_id      int                                 null,
    amount          decimal(10, 2)                      not null,
    method          varchar(30)                         not null,
    status          varchar(20)                         not null,
    external_ref    varchar(100)                        null,
    failure_reason  varchar(200)                        null,
    idempotency_key varchar(100)                        not null,
    created_at      timestamp default CURRENT_TIMESTAMP null,
    index ix_payment_bill_id (bill_id),
    index ix_payment_invoice_id (invoice_id),
    constraint uq_payment_idempotency
        unique (idempotency_key),
    constraint fk_payment_bill
        foreign key (bill_id) references Bill (bill_id),
    constraint fk_payment_invoice
        foreign key (invoice_id) references Invoice (invoice_id),
    constraint ck_payment_exactly_one_target
        check ((`bill_id` is null) <> (`invoice_id` is null))
);
