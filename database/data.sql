use fisport;

INSERT INTO user (username, email, password, phone, birth_day, status, gender, role_id)
VALUES
    ('dattran0901', 'tranquocdat@gmail.com', '$2a$10$s3go5e.GYivSMmrJXG6jceddjfSAbg6O832Sip8XIVNRRLIjXNP6G', '0785819692', '2004-01-09', 'ACTIVE', 'MALE', 1),
    ('johndoe', 'dattranquoc@ gmail.com', '$2a$10$2rQkUgA4sMS5z4QK8f1QXeF6p3iP63PHGQo2og4g0oY6Qp1QjOq9e', '0912345678', '1998-08-15', 'ACTIVE', 'FEMALE', 2),
    ('vitidsarn', 'dattran@ gmail.com', '$2a$10$hswVZ8oCbhAmN9dV7G8nDeeN3Oa0gl7EyxjXg9V8P1c0qpc.Z8W1K', '0934567890', '1990-01-10', 'INACTIVE', 'MALE', 3);

INSERT INTO role (name) VALUES
                            ('ADMIN'),
                            ('USER'),
                            ('OWNER');
