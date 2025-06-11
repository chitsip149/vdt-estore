alter table orders
    alter column created_at set default (CURRENT_TIMESTAMP);