-- 使用者資料表
CREATE SEQUENCE IF NOT EXISTS user_seq_seq
    START WITH 1 INCREMENT BY 1 CACHE 1;

CREATE TABLE IF NOT EXISTS user_t (
    seq BIGSERIAL PRIMARY KEY,
    account VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE user_t IS '使用者資料表';
COMMENT ON COLUMN user_t.seq IS '使用者序號';
COMMENT ON COLUMN user_t.account IS '帳號';
COMMENT ON COLUMN user_t.password IS '密碼';
COMMENT ON COLUMN user_t.email IS '電子信箱';
COMMENT ON COLUMN user_t.createtime IS '建立時間';
COMMENT ON COLUMN user_t.updateTime IS '修改時間';

-- 商店資料表
CREATE TABLE IF NOT EXISTS store_t (
    seq BIGSERIAL PRIMARY KEY,
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
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE store_t IS '商店資訊資料表';
COMMENT ON COLUMN store_t.seq IS '序號';
COMMENT ON COLUMN store_t.store_seq IS '商店序號 關聯user_t.seq';
COMMENT ON COLUMN store_t.store_name IS '商店名稱';
COMMENT ON COLUMN store_t.description IS '商店描述';
COMMENT ON COLUMN store_t.phone IS '商店號碼';
COMMENT ON COLUMN store_t.email IS '商店信箱';
COMMENT ON COLUMN store_t.post_code IS '郵遞區號';
COMMENT ON COLUMN store_t.city IS '縣市';
COMMENT ON COLUMN store_t.district IS '鄉鎮區域';
COMMENT ON COLUMN store_t.street_address IS '詳細地址';
COMMENT ON COLUMN store_t.seats IS '商店座位';
COMMENT ON COLUMN store_t.logo IS '商店商標';
COMMENT ON COLUMN store_t.create_time IS '建立時間';
COMMENT ON COLUMN store_t.update_time IS '修改時間';

-- 營業時間資料表
CREATE TABLE IF NOT EXISTS business_hour_t (
    seq BIGSERIAL PRIMARY KEY,
    store_seq BIGINT NOT NULL,
    is_open BOOLEAN,
    week VARCHAR(20),
    open_time VARCHAR(10),
    close_time VARCHAR(10),
    FOREIGN KEY (store_seq) REFERENCES store_t (seq)
);

COMMENT ON TABLE business_hour_t IS '營業時間資料表';
COMMENT ON COLUMN business_hour_t.seq IS '序號';
COMMENT ON COLUMN business_hour_t.store_seq IS '商店序號 關聯 store_t.seq';
COMMENT ON COLUMN business_hour_t.is_open IS '是否營業';
COMMENT ON COLUMN business_hour_t.week IS '星期幾';
COMMENT ON COLUMN business_hour_t.open_time IS '開始時間';
COMMENT ON COLUMN business_hour_t.close_time IS '結束時間';

-- QR Code 資料表
CREATE TABLE IF NOT EXISTS qrcode_t (
    seq BIGSERIAL PRIMARY KEY,
    store_seq BIGINT NOT NULL,
    qrcode VARCHAR(255) NOT NULL,
    num INTEGER NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_store FOREIGN KEY (store_seq) REFERENCES store_t (seq)
);

COMMENT ON TABLE qrcode_t IS '商店的 QR Code 資料表';
COMMENT ON COLUMN qrcode_t.seq IS 'QR Code 資料表序號';
COMMENT ON COLUMN qrcode_t.store_seq IS '商店序號，關聯 store_t.seq';
COMMENT ON COLUMN qrcode_t.qrcode IS 'QR Code 字串';
COMMENT ON COLUMN qrcode_t.num IS '位子';
COMMENT ON COLUMN qrcode_t.create_time IS '建立時間';
COMMENT ON COLUMN qrcode_t.update_time IS '修改時間';

-- 商品資料表
CREATE TABLE IF NOT EXISTS product_t (
    seq BIGSERIAL PRIMARY KEY,
    product_seq BIGINT,
    product_name VARCHAR(255) NOT NULL,
    product_price INTEGER NOT NULL,
    description TEXT,
    spicy BOOLEAN DEFAULT FALSE,
    coriander BOOLEAN DEFAULT FALSE,
    vinegar BOOLEAN DEFAULT FALSE,
    picture VARCHAR(255),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE product_t IS '商品資料表';
COMMENT ON COLUMN product_t.seq IS '序號';
COMMENT ON COLUMN product_t.product_seq IS '商品序號 關聯store_t.seq';
COMMENT ON COLUMN product_t.product_name IS '商品名稱';
COMMENT ON COLUMN product_t.product_price IS '商品價格';
COMMENT ON COLUMN product_t.description IS '商品描述';
COMMENT ON COLUMN product_t.spicy IS '辣度選項';
COMMENT ON COLUMN product_t.coriander IS '香菜選項';
COMMENT ON COLUMN product_t.vinegar IS '醋選項';
COMMENT ON COLUMN product_t.picture IS '商品圖片';
COMMENT ON COLUMN product_t.create_time IS '建立時間';
COMMENT ON COLUMN product_t.update_time IS '修改時間';

-- 驗證碼資料表
CREATE TABLE IF NOT EXISTS verification_code (
    seq BIGSERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    code VARCHAR(10) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE verification_code IS '驗證碼資料表';
COMMENT ON COLUMN verification_code.seq IS '序號';
COMMENT ON COLUMN verification_code.email IS '電子信箱';
COMMENT ON COLUMN verification_code.code IS '驗證碼';
COMMENT ON COLUMN verification_code.create_time IS '建立時間';

-- 訂單主表
CREATE TABLE IF NOT EXISTS orders_t (
    seq BIGSERIAL PRIMARY KEY,
    order_id VARCHAR(50) NOT NULL UNIQUE,
    store_seq BIGINT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    reject_reason VARCHAR(50) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_store_order FOREIGN KEY (store_seq) REFERENCES store_t(seq)
);

COMMENT ON TABLE orders_t IS '訂單主表';
COMMENT ON COLUMN orders_t.seq IS '序號';
COMMENT ON COLUMN orders_t.order_id IS '訂單編號';
COMMENT ON COLUMN orders_t.store_seq IS '商店序號';
COMMENT ON COLUMN orders_t.total_amount IS '訂單總金額';
COMMENT ON COLUMN orders_t.status IS '訂單狀態';
COMMENT ON COLUMN orders_t.create_time IS '建立時間';
COMMENT ON COLUMN orders_t.update_time IS '修改時間';
COMMENT ON COLUMN orders_t.reject_reason IS '拒絕理由';

-- 訂單明細表
CREATE TABLE IF NOT EXISTS order_items_t (
    seq BIGSERIAL PRIMARY KEY,
    order_seq BIGINT NOT NULL,
    product_seq BIGINT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    spicy_level VARCHAR(20),
    wants_coriander BOOLEAN,
    wants_vinegar BOOLEAN,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_order FOREIGN KEY (order_seq) REFERENCES orders_t(seq),
    CONSTRAINT fk_product FOREIGN KEY (product_seq) REFERENCES product_t(seq)
);

COMMENT ON TABLE order_items_t IS '訂單明細表';
COMMENT ON COLUMN order_items_t.seq IS '序號';
COMMENT ON COLUMN order_items_t.order_seq IS '訂單序號';
COMMENT ON COLUMN order_items_t.product_seq IS '商品序號';
COMMENT ON COLUMN order_items_t.product_name IS '商品名稱';
COMMENT ON COLUMN order_items_t.quantity IS '數量';
COMMENT ON COLUMN order_items_t.price IS '單價';
COMMENT ON COLUMN order_items_t.spicy_level IS '辣度等級';
COMMENT ON COLUMN order_items_t.wants_coriander IS '是否要香菜';
COMMENT ON COLUMN order_items_t.wants_vinegar IS '是否要醋';
COMMENT ON COLUMN order_items_t.create_time IS '建立時間';

