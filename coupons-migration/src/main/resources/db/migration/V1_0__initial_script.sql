-- Table: coupons

-- DROP TABLE coupons;

CREATE TABLE coupons
(
  id                BIGSERIAL              NOT NULL,
  brand_name        CHARACTER VARYING(255),
  can_share         BOOLEAN DEFAULT TRUE,
  circulation       BIGINT                 NOT NULL,
  color             CHARACTER VARYING(255),
  created_at        TIMESTAMP WITHOUT TIME ZONE,
  date_type         CHARACTER VARYING(255) NOT NULL,
  fixed_begin_term  INTEGER,
  fixed_term        INTEGER,
  time_range_end    TIMESTAMP WITHOUT TIME ZONE,
  time_range_start  TIMESTAMP WITHOUT TIME ZONE,
  description       CHARACTER VARYING(1024),
  custom_link_name  CHARACTER VARYING(1024),
  custom_link_title CHARACTER VARYING(1024),
  custom_link_url   CHARACTER VARYING(1024),
  get_limit         INTEGER                NOT NULL,
  issuer_id         CHARACTER VARYING(255),
  issuer_type       CHARACTER VARYING(255) NOT NULL,
  notice            TEXT,
  rev               INTEGER,
  service_phone     CHARACTER VARYING(20),
  sku               BIGINT                 NOT NULL,
  status            CHARACTER VARYING(255),
  subtitle          CHARACTER VARYING(255),
  target            CHARACTER VARYING(255) NOT NULL,
  title             CHARACTER VARYING(255) NOT NULL,
  type              CHARACTER VARYING(255) NOT NULL,
  updated_at        TIMESTAMP WITHOUT TIME ZONE,
  value             REAL,
  CONSTRAINT coupons_pkey PRIMARY KEY (id)
)
WITH (
OIDS = FALSE
);
ALTER TABLE coupons
  OWNER TO coupon;

-- Index: idxecux1slmygptbsiyl4tud2yh

-- DROP INDEX idxecux1slmygptbsiyl4tud2yh;

CREATE INDEX idxecux1slmygptbsiyl4tud2yh
  ON coupons
  USING BTREE
  (issuer_id COLLATE pg_catalog."default");

-- Index: idxnictoh3y71ixl3fcgmpnfn3o

-- DROP INDEX idxnictoh3y71ixl3fcgmpnfn3o;

CREATE INDEX idxnictoh3y71ixl3fcgmpnfn3o
  ON coupons
  USING BTREE
  (created_at);

-- Table: stores

-- DROP TABLE stores;

CREATE TABLE stores
(
  coupon_id BIGINT NOT NULL,
  store     CHARACTER VARYING(255),
  CONSTRAINT fki364461pymyj1usbx058tvjs FOREIGN KEY (coupon_id)
  REFERENCES coupons (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
);
ALTER TABLE stores
  OWNER TO coupon;

-- Table: sell_coupons

-- DROP TABLE sell_coupons;

CREATE TABLE sell_coupons
(
  id         BIGSERIAL NOT NULL,
  created_at TIMESTAMP WITHOUT TIME ZONE,
  open_id    CHARACTER VARYING(255),
  rev        INTEGER,
  sku        INTEGER,
  updated_at TIMESTAMP WITHOUT TIME ZONE,
  user_id    CHARACTER VARYING(255),
  coupon_id  BIGINT,
  CONSTRAINT sell_coupons_pkey PRIMARY KEY (id),
  CONSTRAINT fkb0wlhjyctpg0nnm8gprdqhe0t FOREIGN KEY (coupon_id)
  REFERENCES coupons (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
);
ALTER TABLE sell_coupons
  OWNER TO coupon;

-- Index: idx1t144qblsmrxwwt7isnypnqf8

-- DROP INDEX idx1t144qblsmrxwwt7isnypnqf8;

CREATE INDEX idx1t144qblsmrxwwt7isnypnqf8
  ON sell_coupons
  USING BTREE
  (open_id COLLATE pg_catalog."default");

-- Index: idxepeonyueb4ryq8n3cl7fd2tkw

-- DROP INDEX idxepeonyueb4ryq8n3cl7fd2tkw;

CREATE INDEX idxepeonyueb4ryq8n3cl7fd2tkw
  ON sell_coupons
  USING BTREE
  (open_id COLLATE pg_catalog."default", coupon_id);

-- Index: idxi4r3lx6ocer4fbo69lnkp4o8g

-- DROP INDEX idxi4r3lx6ocer4fbo69lnkp4o8g;

CREATE INDEX idxi4r3lx6ocer4fbo69lnkp4o8g
  ON sell_coupons
  USING BTREE
  (created_at);

-- Index: idxss90hj9tk9tumbonfv8fubf49

-- DROP INDEX idxss90hj9tk9tumbonfv8fubf49;

CREATE INDEX idxss90hj9tk9tumbonfv8fubf49
  ON sell_coupons
  USING BTREE
  (coupon_id);

-- Table: sell_coupon_grants

-- DROP TABLE sell_coupon_grants;

CREATE TABLE sell_coupon_grants
(
  id             CHARACTER VARYING(255) NOT NULL,
  count          INTEGER                NOT NULL,
  created_at     TIMESTAMP WITHOUT TIME ZONE,
  expired_at     TIMESTAMP WITHOUT TIME ZONE,
  open_id        CHARACTER VARYING(255) NOT NULL,
  rev            INTEGER,
  status         CHARACTER VARYING(255) NOT NULL,
  updated_at     TIMESTAMP WITHOUT TIME ZONE,
  sell_coupon_id BIGINT,
  CONSTRAINT sell_coupon_grants_pkey PRIMARY KEY (id),
  CONSTRAINT fktg5obd9sd8egbgr2bhco77oa9 FOREIGN KEY (sell_coupon_id)
  REFERENCES sell_coupons (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
);
ALTER TABLE sell_coupon_grants
  OWNER TO coupon;

-- Index: idx3hr4ow5olar5lce55170r6dgb

-- DROP INDEX idx3hr4ow5olar5lce55170r6dgb;

CREATE INDEX idx3hr4ow5olar5lce55170r6dgb
  ON sell_coupon_grants
  USING BTREE
  (created_at);

-- Index: idxb2r6h34l7ntrmfxapstyxky6f

-- DROP INDEX idxb2r6h34l7ntrmfxapstyxky6f;

CREATE INDEX idxb2r6h34l7ntrmfxapstyxky6f
  ON sell_coupon_grants
  USING BTREE
  (open_id COLLATE pg_catalog."default");

-- Table: sell_coupon_grant_entries

-- DROP TABLE sell_coupon_grant_entries;

CREATE TABLE sell_coupon_grant_entries
(
  id                   BIGSERIAL NOT NULL,
  coupon_code          CHARACTER VARYING(255),
  created_at           TIMESTAMP WITHOUT TIME ZONE,
  open_id              CHARACTER VARYING(255),
  rev                  INTEGER,
  sell_open_id         CHARACTER VARYING(255),
  updated_at           TIMESTAMP WITHOUT TIME ZONE,
  user_id              CHARACTER VARYING(255),
  sell_coupon_grant_id CHARACTER VARYING(255),
  CONSTRAINT sell_coupon_grant_entries_pkey PRIMARY KEY (id),
  CONSTRAINT fknuxwayroo0mr74dnvdsw0yk2y FOREIGN KEY (sell_coupon_grant_id)
  REFERENCES sell_coupon_grants (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
);
ALTER TABLE sell_coupon_grant_entries
  OWNER TO coupon;

-- Index: idx4s89psb300wlphuyh0upabryo

-- DROP INDEX idx4s89psb300wlphuyh0upabryo;

CREATE INDEX idx4s89psb300wlphuyh0upabryo
  ON sell_coupon_grant_entries
  USING BTREE
  (created_at);

-- Index: idxhp0a36wd4locp04lpmwnqjiia

-- DROP INDEX idxhp0a36wd4locp04lpmwnqjiia;

CREATE INDEX idxhp0a36wd4locp04lpmwnqjiia
  ON sell_coupon_grant_entries
  USING BTREE
  (sell_open_id COLLATE pg_catalog."default");

-- Index: idxk7pxo5vaxerj9se4m58v1tgac

-- DROP INDEX idxk7pxo5vaxerj9se4m58v1tgac;

CREATE INDEX idxk7pxo5vaxerj9se4m58v1tgac
  ON sell_coupon_grant_entries
  USING BTREE
  (coupon_code COLLATE pg_catalog."default");

-- Index: idxt2k7v1duqdfekgirj5g3p3yf3

-- DROP INDEX idxt2k7v1duqdfekgirj5g3p3yf3;

CREATE INDEX idxt2k7v1duqdfekgirj5g3p3yf3
  ON sell_coupon_grant_entries
  USING BTREE
  (sell_open_id COLLATE pg_catalog."default", created_at);

-- Table: user_coupons

-- DROP TABLE user_coupons;

CREATE TABLE user_coupons
(
  id           BIGSERIAL              NOT NULL,
  begin_date   TIMESTAMP WITHOUT TIME ZONE,
  consumed_at  TIMESTAMP WITHOUT TIME ZONE,
  coupon_code  CHARACTER VARYING(255) NOT NULL,
  created_at   TIMESTAMP WITHOUT TIME ZONE,
  end_date     TIMESTAMP WITHOUT TIME ZONE,
  from_open_id CHARACTER VARYING(255),
  from_user_id CHARACTER VARYING(255),
  got_at       TIMESTAMP WITHOUT TIME ZONE,
  open_id      CHARACTER VARYING(255),
  out_id       CHARACTER VARYING(255),
  rev          INTEGER,
  status       CHARACTER VARYING(255),
  store_id     CHARACTER VARYING(255),
  updated_at   TIMESTAMP WITHOUT TIME ZONE,
  user_id      CHARACTER VARYING(255),
  coupon_id    BIGINT,
  CONSTRAINT user_coupons_pkey PRIMARY KEY (id),
  CONSTRAINT fk9oi3p5xyfe4j32xs54nn7mi20 FOREIGN KEY (coupon_id)
  REFERENCES coupons (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_prcg26hxc9j9bukr3as380occ UNIQUE (coupon_code)
)
WITH (
OIDS = FALSE
);
ALTER TABLE user_coupons
  OWNER TO coupon;

-- Index: idx1ypmid38xamgn2lt7xx3auj6g

-- DROP INDEX idx1ypmid38xamgn2lt7xx3auj6g;

CREATE INDEX idx1ypmid38xamgn2lt7xx3auj6g
  ON user_coupons
  USING BTREE
  (open_id COLLATE pg_catalog."default");

-- Index: idx8n8e0ty12hklqikk9vdm1kiby

-- DROP INDEX idx8n8e0ty12hklqikk9vdm1kiby;

CREATE INDEX idx8n8e0ty12hklqikk9vdm1kiby
  ON user_coupons
  USING BTREE
  (begin_date);

-- Index: idxbmhdhcm3gwjws604pwiy5buy8

-- DROP INDEX idxbmhdhcm3gwjws604pwiy5buy8;

CREATE INDEX idxbmhdhcm3gwjws604pwiy5buy8
  ON user_coupons
  USING BTREE
  (end_date);

-- Index: idxieg0c07r5txlfmredodexvvvq

-- DROP INDEX idxieg0c07r5txlfmredodexvvvq;

CREATE INDEX idxieg0c07r5txlfmredodexvvvq
  ON user_coupons
  USING BTREE
  (open_id COLLATE pg_catalog."default", end_date);

-- Index: idxprcg26hxc9j9bukr3as380occ

-- DROP INDEX idxprcg26hxc9j9bukr3as380occ;

CREATE INDEX idxprcg26hxc9j9bukr3as380occ
  ON user_coupons
  USING BTREE
  (coupon_code COLLATE pg_catalog."default");

-- Index: idxt8yaxhnqy3v65wtm7cooartao

-- DROP INDEX idxt8yaxhnqy3v65wtm7cooartao;

CREATE INDEX idxt8yaxhnqy3v65wtm7cooartao
  ON user_coupons
  USING BTREE
  (consumed_at);

-- Table: user_coupon_shares

-- DROP TABLE user_coupon_shares;

CREATE TABLE user_coupon_shares
(
  id         CHARACTER VARYING(255) NOT NULL,
  count      INTEGER                NOT NULL,
  created_at TIMESTAMP WITHOUT TIME ZONE,
  expired_at TIMESTAMP WITHOUT TIME ZONE,
  open_id    CHARACTER VARYING(255),
  rev        INTEGER,
  status     CHARACTER VARYING(255) NOT NULL,
  updated_at TIMESTAMP WITHOUT TIME ZONE,
  user_id    CHARACTER VARYING(255),
  coupon_id  BIGINT,
  CONSTRAINT user_coupon_shares_pkey PRIMARY KEY (id),
  CONSTRAINT fkamexyde3l0uypwfefw0ugsgss FOREIGN KEY (coupon_id)
  REFERENCES coupons (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
);
ALTER TABLE user_coupon_shares
  OWNER TO coupon;

-- Index: idx2p9a0ampgc3jab0r5nyqpy8t1

-- DROP INDEX idx2p9a0ampgc3jab0r5nyqpy8t1;

CREATE INDEX idx2p9a0ampgc3jab0r5nyqpy8t1
  ON user_coupon_shares
  USING BTREE
  (created_at);

-- Index: idxof1340y7h443kciw12x7np5hp

-- DROP INDEX idxof1340y7h443kciw12x7np5hp;

CREATE INDEX idxof1340y7h443kciw12x7np5hp
  ON user_coupon_shares
  USING BTREE
  (open_id COLLATE pg_catalog."default");

-- Table: user_coupon_share_entries

-- DROP TABLE user_coupon_share_entries;

CREATE TABLE user_coupon_share_entries
(
  id                   BIGSERIAL NOT NULL,
  created_at           TIMESTAMP WITHOUT TIME ZONE,
  got                  BOOLEAN,
  got_at               TIMESTAMP WITHOUT TIME ZONE,
  open_id              CHARACTER VARYING(255),
  rev                  INTEGER,
  updated_at           TIMESTAMP WITHOUT TIME ZONE,
  user_coupon_id       BIGINT    NOT NULL,
  user_id              CHARACTER VARYING(255),
  user_coupon_share_id CHARACTER VARYING(255),
  CONSTRAINT user_coupon_share_entries_pkey PRIMARY KEY (id),
  CONSTRAINT fk6rbswvc3i66c0tgof7o81jiin FOREIGN KEY (user_coupon_share_id)
  REFERENCES user_coupon_shares (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
);
ALTER TABLE user_coupon_share_entries
  OWNER TO coupon;
