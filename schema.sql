-- auto-generated definition
create table benchmark_records
(
    id         serial
        primary key,
    game_name  varchar(100),
    avg_fps    double precision,
    cpu_temp   double precision,
    gpu_temp   double precision,
    cpu_usage  double precision,
    gpu_usage  double precision,
    created_at timestamp default CURRENT_TIMESTAMP
);

alter table benchmark_records
    owner to postgres;
