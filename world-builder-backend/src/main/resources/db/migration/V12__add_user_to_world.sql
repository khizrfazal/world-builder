ALTER TABLE worldbuilder.worlds
ADD COLUMN user_id UUID;

ALTER TABLE worlds
ADD CONSTRAINT fk_world_user
FOREIGN KEY (user_id) REFERENCES users(id);