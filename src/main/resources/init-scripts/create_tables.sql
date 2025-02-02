CREATE SEQUENCE IF NOT EXISTS user_seq_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS "user_t" (
    seq BIGINT DEFAULT nextval('user_seq_seq') PRIMARY KEY,
    account VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    createtime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updateTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER SEQUENCE user_seq_seq OWNED BY "user_t".seq;

CREATE INDEX IF NOT EXISTS idx_user_account ON "user_t" (account);
CREATE INDEX IF NOT EXISTS idx_user_email ON "user_t" (email);

COMMENT ON TABLE "user_t" IS '使用者資料表';
COMMENT ON COLUMN "user_t".seq IS '使用者序號';
COMMENT ON COLUMN "user_t".account IS '帳號';
COMMENT ON COLUMN "user_t".password IS '密碼';
COMMENT ON COLUMN "user_t".email IS '電子信箱';
COMMENT ON COLUMN "user_t".createtime IS '建立時間';
COMMENT ON COLUMN "user_t".updateTime IS '修改時間';

CREATE SEQUENCE IF NOT EXISTS verification_code_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

CREATE TABLE IF NOT EXISTS "verification_code" (
    seq BIGINT DEFAULT nextval('verification_code_seq') PRIMARY KEY,
    email VARCHAR(50) NOT NULL,
    code VARCHAR(6) NOT NULL,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER SEQUENCE verification_code_seq OWNED BY "verification_code".seq;

CREATE INDEX IF NOT EXISTS idx_verification_code_email ON "verification_code" (email);

COMMENT ON TABLE "verification_code" IS '驗證碼資料表';
COMMENT ON COLUMN "verification_code".seq IS '序號';
COMMENT ON COLUMN "verification_code".email IS '電子信箱';
COMMENT ON COLUMN "verification_code".code IS '驗證碼';
COMMENT ON COLUMN "verification_code".create_time IS '建立時間';

CREATE SEQUENCE IF NOT EXISTS store_seq_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

CREATE TABLE IF NOT EXISTS "store_t" (
    seq BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,  
    store_seq BIGINT, 
    store_name VARCHAR(255),  
    description TEXT, 
    phone VARCHAR(50), 
    email VARCHAR(100), 
    post_code VARCHAR(20) NOT NULL,  
    city VARCHAR(100) NOT NULL, 
    district VARCHAR(100) NOT NULL,  
    street_address VARCHAR(255) NOT NULL,
    logo VARCHAR(255),  
    seats INTEGER DEFAULT 0,  
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP 
);

ALTER SEQUENCE store_seq_seq OWNED BY "store_t".seq;

CREATE INDEX IF NOT EXISTS idx_store_store_seq ON "store_t" (store_seq);
CREATE INDEX IF NOT EXISTS idx_store_store_name ON "store_t" (store_name);
CREATE INDEX IF NOT EXISTS idx_store_email ON "store_t" (email);

COMMENT ON TABLE "store_t" IS '商店資訊資料表';
COMMENT ON COLUMN "store_t".seq IS '序號';
COMMENT ON COLUMN "store_t".store_seq IS '商店序號 關聯user_t.seq';
COMMENT ON COLUMN "store_t".store_name IS '商店名稱';
COMMENT ON COLUMN "store_t".description IS '商店描述';
COMMENT ON COLUMN "store_t".phone IS '商店號碼';
COMMENT ON COLUMN "store_t".email IS '商店信箱';
COMMENT ON COLUMN "store_t".post_code IS '郵遞區號';
COMMENT ON COLUMN "store_t".city IS '縣市';
COMMENT ON COLUMN "store_t".district IS '鄉鎮區域';
COMMENT ON COLUMN "store_t".street_address IS '詳細地址';
COMMENT ON COLUMN "store_t".seats IS '商店座位';
COMMENT ON COLUMN "store_t".logo IS '商店商標';
COMMENT ON COLUMN "store_t".create_time IS '建立時間';
COMMENT ON COLUMN "store_t".update_time IS '修改時間';

CREATE TABLE IF NOT EXISTS "business_hour_t" (
    seq BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    store_seq BIGINT,
    is_open BOOLEAN,
    week VARCHAR(20),
    open_time VARCHAR(10),
    close_time VARCHAR(10)
);

CREATE INDEX IF NOT EXISTS idx_business_hour_store_seq ON "business_hour_t" (store_seq);
CREATE INDEX IF NOT EXISTS idx_business_hour_week ON "business_hour_t" (week);

COMMENT ON TABLE "business_hour_t" IS '營業時間資料表';
COMMENT ON COLUMN "business_hour_t".seq IS '序號';
COMMENT ON COLUMN "business_hour_t".store_seq IS '商店序號 關聯 store_t.seq';
COMMENT ON COLUMN "business_hour_t".is_open IS '是否營業';
COMMENT ON COLUMN "business_hour_t".week IS '星期幾';
COMMENT ON COLUMN "business_hour_t".open_time IS '開始時間';
COMMENT ON COLUMN "business_hour_t".close_time IS '結束時間';

CREATE SEQUENCE IF NOT EXISTS qrcode_seq_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS "qrcode_t" (
    seq BIGINT DEFAULT nextval('qrcode_seq_seq') PRIMARY KEY,  -- 序號，使用序列自動生成
    store_seq BIGINT NOT NULL,  -- 商店序號，關聯到 store_t.seq
    qrcode VARCHAR(255) NOT NULL,  -- qrcode 字串
    num INTEGER NOT NULL,  -- 位子
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- 建立時間
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- 修改時間
    CONSTRAINT fk_store FOREIGN KEY (store_seq) REFERENCES "store_t" (seq)  -- 外鍵約束，關聯到 store_t 資料表的 seq
);

ALTER SEQUENCE qrcode_seq_seq OWNED BY "qrcode_t".seq;

CREATE INDEX IF NOT EXISTS idx_qrcode_store_seq ON "qrcode_t" (store_seq);
CREATE INDEX IF NOT EXISTS idx_qrcode_qrcode ON "qrcode_t" (qrcode);

COMMENT ON TABLE "qrcode_t" IS '商店的 QR Code 資料表';
COMMENT ON COLUMN "qrcode_t".seq IS 'QR Code 資料表序號';
COMMENT ON COLUMN "qrcode_t".store_seq IS '商店序號，關聯 store_t.seq';
COMMENT ON COLUMN "qrcode_t".qrcode IS 'QR Code 字串';
COMMENT ON COLUMN "qrcode_t".num IS '位子';
COMMENT ON COLUMN "qrcode_t".create_time IS '建立時間';
COMMENT ON COLUMN "qrcode_t".update_time IS '修改時間';
