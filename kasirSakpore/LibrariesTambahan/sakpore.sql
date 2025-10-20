--
-- PostgreSQL database dump
--

\restrict 908hqml0rnz4MflTP1utwOgrgteqkqtIXPkc76IKAn7Ez3ra9COHgVVQhV7kUZ1

-- Dumped from database version 13.22
-- Dumped by pg_dump version 13.22

-- Started on 2025-10-20 14:44:43

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 216 (class 1259 OID 24811)
-- Name: barang; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.barang (
    kodebarang integer NOT NULL,
    skubarang character varying(50) NOT NULL,
    nama character varying(100) NOT NULL,
    hargabarang numeric(12,2) NOT NULL,
    stok integer,
    kategori character varying(100),
    gambar character varying(255),
    hargapokok numeric(12,2),
    ppn numeric(12,2),
    satuan character varying(200)
);


ALTER TABLE public.barang OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 24586)
-- Name: barangmasuk; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.barangmasuk (
    idbarangmasuk integer NOT NULL,
    kodebarang integer,
    nama character varying(100),
    satuan character varying(50),
    jumlahmasuk integer,
    hargabarang numeric(12,2),
    totalharga numeric(12,2),
    tanggal date,
    supplier character varying(100),
    skubarang character varying(200)
);


ALTER TABLE public.barangmasuk OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 24589)
-- Name: barangmasuk_idbarangmasuk_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.barangmasuk_idbarangmasuk_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.barangmasuk_idbarangmasuk_seq OWNER TO postgres;

--
-- TOC entry 3102 (class 0 OID 0)
-- Dependencies: 201
-- Name: barangmasuk_idbarangmasuk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.barangmasuk_idbarangmasuk_seq OWNED BY public.barangmasuk.idbarangmasuk;


--
-- TOC entry 202 (class 1259 OID 24591)
-- Name: detailtransaksi; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.detailtransaksi (
    iddetailtransaksi integer NOT NULL,
    kodebarang integer,
    idtransaksi integer,
    namabarang character varying(100),
    jumlah integer,
    harga numeric(12,2),
    keterangan text,
    subtotal numeric(12,2)
);


ALTER TABLE public.detailtransaksi OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 24597)
-- Name: detailtransaksi_iddetailtransaksi_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.detailtransaksi_iddetailtransaksi_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.detailtransaksi_iddetailtransaksi_seq OWNER TO postgres;

--
-- TOC entry 3103 (class 0 OID 0)
-- Dependencies: 203
-- Name: detailtransaksi_iddetailtransaksi_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.detailtransaksi_iddetailtransaksi_seq OWNED BY public.detailtransaksi.iddetailtransaksi;


--
-- TOC entry 204 (class 1259 OID 24599)
-- Name: kategori; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.kategori (
    idkategori integer NOT NULL,
    namakategori character varying(100) NOT NULL
);


ALTER TABLE public.kategori OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 24602)
-- Name: kategori_idkategori_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.kategori_idkategori_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.kategori_idkategori_seq OWNER TO postgres;

--
-- TOC entry 3104 (class 0 OID 0)
-- Dependencies: 205
-- Name: kategori_idkategori_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.kategori_idkategori_seq OWNED BY public.kategori.idkategori;


--
-- TOC entry 206 (class 1259 OID 24604)
-- Name: keuangan; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.keuangan (
    idkeuangan integer NOT NULL,
    idasal integer,
    jeniskeuangan character varying(50),
    masuk numeric(12,2) DEFAULT 0,
    keluar numeric(12,2) DEFAULT 0,
    tanggal date
);


ALTER TABLE public.keuangan OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 24609)
-- Name: keuangan_idkeuangan_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.keuangan_idkeuangan_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.keuangan_idkeuangan_seq OWNER TO postgres;

--
-- TOC entry 3105 (class 0 OID 0)
-- Dependencies: 207
-- Name: keuangan_idkeuangan_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.keuangan_idkeuangan_seq OWNED BY public.keuangan.idkeuangan;


--
-- TOC entry 208 (class 1259 OID 24611)
-- Name: metodepembayaran; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.metodepembayaran (
    idmetodepembayaran integer NOT NULL,
    namametodepembayaran character varying(50) NOT NULL
);


ALTER TABLE public.metodepembayaran OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 24614)
-- Name: metodepembayaran_idmetodepembayaran_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.metodepembayaran_idmetodepembayaran_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.metodepembayaran_idmetodepembayaran_seq OWNER TO postgres;

--
-- TOC entry 3106 (class 0 OID 0)
-- Dependencies: 209
-- Name: metodepembayaran_idmetodepembayaran_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.metodepembayaran_idmetodepembayaran_seq OWNED BY public.metodepembayaran.idmetodepembayaran;


--
-- TOC entry 210 (class 1259 OID 24616)
-- Name: pengguna; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pengguna (
    idpengguna integer NOT NULL,
    username character varying(50) NOT NULL,
    password character varying(255) NOT NULL,
    role character varying(20) NOT NULL,
    status character varying(20) DEFAULT 'aktif'::character varying,
    nama character varying(100)
);


ALTER TABLE public.pengguna OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 24620)
-- Name: pengguna_idpengguna_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pengguna_idpengguna_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.pengguna_idpengguna_seq OWNER TO postgres;

--
-- TOC entry 3107 (class 0 OID 0)
-- Dependencies: 211
-- Name: pengguna_idpengguna_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pengguna_idpengguna_seq OWNED BY public.pengguna.idpengguna;


--
-- TOC entry 218 (class 1259 OID 24821)
-- Name: riwayat_login; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.riwayat_login (
    id integer NOT NULL,
    idpengguna integer,
    username character varying(50),
    nama_pemakai character varying(100),
    waktu_login timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.riwayat_login OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 24819)
-- Name: riwayat_login_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.riwayat_login_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.riwayat_login_id_seq OWNER TO postgres;

--
-- TOC entry 3108 (class 0 OID 0)
-- Dependencies: 217
-- Name: riwayat_login_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.riwayat_login_id_seq OWNED BY public.riwayat_login.id;


--
-- TOC entry 212 (class 1259 OID 24622)
-- Name: supplier; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.supplier (
    idsupplier integer NOT NULL,
    namasupplier character varying(100) NOT NULL,
    notelp character varying(20),
    status character varying(20) DEFAULT 'aktif'::character varying,
    alamat text
);


ALTER TABLE public.supplier OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 24629)
-- Name: supplier_idsupplier_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.supplier_idsupplier_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.supplier_idsupplier_seq OWNER TO postgres;

--
-- TOC entry 3109 (class 0 OID 0)
-- Dependencies: 213
-- Name: supplier_idsupplier_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.supplier_idsupplier_seq OWNED BY public.supplier.idsupplier;


--
-- TOC entry 214 (class 1259 OID 24631)
-- Name: transaksi; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.transaksi (
    idtransaksi integer NOT NULL,
    notransaksi character varying(50) NOT NULL,
    namapengguna character varying(50),
    tgl_transaksi timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    subtotal numeric(12,2) DEFAULT 0,
    diskon numeric(12,2) DEFAULT 0,
    grand_total numeric(12,2) DEFAULT 0,
    metodepembayaran character varying(50)
);


ALTER TABLE public.transaksi OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 24638)
-- Name: transaksi_idtransaksi_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.transaksi_idtransaksi_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.transaksi_idtransaksi_seq OWNER TO postgres;

--
-- TOC entry 3110 (class 0 OID 0)
-- Dependencies: 215
-- Name: transaksi_idtransaksi_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.transaksi_idtransaksi_seq OWNED BY public.transaksi.idtransaksi;


--
-- TOC entry 2905 (class 2604 OID 24641)
-- Name: barangmasuk idbarangmasuk; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barangmasuk ALTER COLUMN idbarangmasuk SET DEFAULT nextval('public.barangmasuk_idbarangmasuk_seq'::regclass);


--
-- TOC entry 2906 (class 2604 OID 24642)
-- Name: detailtransaksi iddetailtransaksi; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detailtransaksi ALTER COLUMN iddetailtransaksi SET DEFAULT nextval('public.detailtransaksi_iddetailtransaksi_seq'::regclass);


--
-- TOC entry 2907 (class 2604 OID 24643)
-- Name: kategori idkategori; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kategori ALTER COLUMN idkategori SET DEFAULT nextval('public.kategori_idkategori_seq'::regclass);


--
-- TOC entry 2910 (class 2604 OID 24644)
-- Name: keuangan idkeuangan; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.keuangan ALTER COLUMN idkeuangan SET DEFAULT nextval('public.keuangan_idkeuangan_seq'::regclass);


--
-- TOC entry 2911 (class 2604 OID 24645)
-- Name: metodepembayaran idmetodepembayaran; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.metodepembayaran ALTER COLUMN idmetodepembayaran SET DEFAULT nextval('public.metodepembayaran_idmetodepembayaran_seq'::regclass);


--
-- TOC entry 2913 (class 2604 OID 24646)
-- Name: pengguna idpengguna; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pengguna ALTER COLUMN idpengguna SET DEFAULT nextval('public.pengguna_idpengguna_seq'::regclass);


--
-- TOC entry 2921 (class 2604 OID 24824)
-- Name: riwayat_login id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.riwayat_login ALTER COLUMN id SET DEFAULT nextval('public.riwayat_login_id_seq'::regclass);


--
-- TOC entry 2915 (class 2604 OID 24647)
-- Name: supplier idsupplier; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.supplier ALTER COLUMN idsupplier SET DEFAULT nextval('public.supplier_idsupplier_seq'::regclass);


--
-- TOC entry 2920 (class 2604 OID 24648)
-- Name: transaksi idtransaksi; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaksi ALTER COLUMN idtransaksi SET DEFAULT nextval('public.transaksi_idtransaksi_seq'::regclass);


--
-- TOC entry 3094 (class 0 OID 24811)
-- Dependencies: 216
-- Data for Name: barang; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.barang (kodebarang, skubarang, nama, hargabarang, stok, kategori, gambar, hargapokok, ppn, satuan) FROM stdin;
\.


--
-- TOC entry 3078 (class 0 OID 24586)
-- Dependencies: 200
-- Data for Name: barangmasuk; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.barangmasuk (idbarangmasuk, kodebarang, nama, satuan, jumlahmasuk, hargabarang, totalharga, tanggal, supplier, skubarang) FROM stdin;
\.


--
-- TOC entry 3080 (class 0 OID 24591)
-- Dependencies: 202
-- Data for Name: detailtransaksi; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.detailtransaksi (iddetailtransaksi, kodebarang, idtransaksi, namabarang, jumlah, harga, keterangan, subtotal) FROM stdin;
\.


--
-- TOC entry 3082 (class 0 OID 24599)
-- Dependencies: 204
-- Data for Name: kategori; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.kategori (idkategori, namakategori) FROM stdin;
\.


--
-- TOC entry 3084 (class 0 OID 24604)
-- Dependencies: 206
-- Data for Name: keuangan; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.keuangan (idkeuangan, idasal, jeniskeuangan, masuk, keluar, tanggal) FROM stdin;
\.


--
-- TOC entry 3086 (class 0 OID 24611)
-- Dependencies: 208
-- Data for Name: metodepembayaran; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.metodepembayaran (idmetodepembayaran, namametodepembayaran) FROM stdin;
\.


--
-- TOC entry 3088 (class 0 OID 24616)
-- Dependencies: 210
-- Data for Name: pengguna; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pengguna (idpengguna, username, password, role, status, nama) FROM stdin;
2	kasir1	kasir123	kasir	aktif	SISISIS
4	kasir2	kasir123	kasir	aktif	VIRGAN JUNI MAS UDI
3	manager	manager123	manager	aktif	sukisukidaisuku
1	admin	admin123	admin	aktif	jepri nichol
\.


--
-- TOC entry 3096 (class 0 OID 24821)
-- Dependencies: 218
-- Data for Name: riwayat_login; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.riwayat_login (id, idpengguna, username, nama_pemakai, waktu_login) FROM stdin;
36	1	admin	jepri nichol	2025-10-20 14:42:32.199806
37	1	admin	virgan	2025-10-20 14:43:47.919766
\.


--
-- TOC entry 3090 (class 0 OID 24622)
-- Dependencies: 212
-- Data for Name: supplier; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.supplier (idsupplier, namasupplier, notelp, status, alamat) FROM stdin;
\.


--
-- TOC entry 3092 (class 0 OID 24631)
-- Dependencies: 214
-- Data for Name: transaksi; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.transaksi (idtransaksi, notransaksi, namapengguna, tgl_transaksi, subtotal, diskon, grand_total, metodepembayaran) FROM stdin;
\.


--
-- TOC entry 3111 (class 0 OID 0)
-- Dependencies: 201
-- Name: barangmasuk_idbarangmasuk_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.barangmasuk_idbarangmasuk_seq', 18, true);


--
-- TOC entry 3112 (class 0 OID 0)
-- Dependencies: 203
-- Name: detailtransaksi_iddetailtransaksi_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.detailtransaksi_iddetailtransaksi_seq', 84, true);


--
-- TOC entry 3113 (class 0 OID 0)
-- Dependencies: 205
-- Name: kategori_idkategori_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.kategori_idkategori_seq', 4, true);


--
-- TOC entry 3114 (class 0 OID 0)
-- Dependencies: 207
-- Name: keuangan_idkeuangan_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.keuangan_idkeuangan_seq', 103, true);


--
-- TOC entry 3115 (class 0 OID 0)
-- Dependencies: 209
-- Name: metodepembayaran_idmetodepembayaran_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.metodepembayaran_idmetodepembayaran_seq', 3, true);


--
-- TOC entry 3116 (class 0 OID 0)
-- Dependencies: 211
-- Name: pengguna_idpengguna_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pengguna_idpengguna_seq', 5, true);


--
-- TOC entry 3117 (class 0 OID 0)
-- Dependencies: 217
-- Name: riwayat_login_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.riwayat_login_id_seq', 37, true);


--
-- TOC entry 3118 (class 0 OID 0)
-- Dependencies: 213
-- Name: supplier_idsupplier_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.supplier_idsupplier_seq', 11, true);


--
-- TOC entry 3119 (class 0 OID 0)
-- Dependencies: 215
-- Name: transaksi_idtransaksi_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.transaksi_idtransaksi_seq', 39, true);


--
-- TOC entry 2944 (class 2606 OID 24818)
-- Name: barang barang_new_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barang
    ADD CONSTRAINT barang_new_pkey PRIMARY KEY (kodebarang);


--
-- TOC entry 2924 (class 2606 OID 24652)
-- Name: barangmasuk barangmasuk_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barangmasuk
    ADD CONSTRAINT barangmasuk_pkey PRIMARY KEY (idbarangmasuk);


--
-- TOC entry 2926 (class 2606 OID 24654)
-- Name: detailtransaksi detailtransaksi_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detailtransaksi
    ADD CONSTRAINT detailtransaksi_pkey PRIMARY KEY (iddetailtransaksi);


--
-- TOC entry 2928 (class 2606 OID 24656)
-- Name: kategori kategori_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kategori
    ADD CONSTRAINT kategori_pkey PRIMARY KEY (idkategori);


--
-- TOC entry 2930 (class 2606 OID 24658)
-- Name: keuangan keuangan_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.keuangan
    ADD CONSTRAINT keuangan_pkey PRIMARY KEY (idkeuangan);


--
-- TOC entry 2932 (class 2606 OID 24660)
-- Name: metodepembayaran metodepembayaran_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.metodepembayaran
    ADD CONSTRAINT metodepembayaran_pkey PRIMARY KEY (idmetodepembayaran);


--
-- TOC entry 2934 (class 2606 OID 24662)
-- Name: pengguna pengguna_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pengguna
    ADD CONSTRAINT pengguna_pkey PRIMARY KEY (idpengguna);


--
-- TOC entry 2936 (class 2606 OID 24664)
-- Name: pengguna pengguna_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pengguna
    ADD CONSTRAINT pengguna_username_key UNIQUE (username);


--
-- TOC entry 2946 (class 2606 OID 24827)
-- Name: riwayat_login riwayat_login_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.riwayat_login
    ADD CONSTRAINT riwayat_login_pkey PRIMARY KEY (id);


--
-- TOC entry 2938 (class 2606 OID 24666)
-- Name: supplier supplier_namasupplier_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.supplier
    ADD CONSTRAINT supplier_namasupplier_key UNIQUE (namasupplier);


--
-- TOC entry 2940 (class 2606 OID 24668)
-- Name: supplier supplier_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.supplier
    ADD CONSTRAINT supplier_pkey PRIMARY KEY (idsupplier);


--
-- TOC entry 2942 (class 2606 OID 24670)
-- Name: transaksi transaksi_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaksi
    ADD CONSTRAINT transaksi_pkey PRIMARY KEY (idtransaksi);


--
-- TOC entry 2947 (class 2606 OID 24828)
-- Name: riwayat_login riwayat_login_idpengguna_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.riwayat_login
    ADD CONSTRAINT riwayat_login_idpengguna_fkey FOREIGN KEY (idpengguna) REFERENCES public.pengguna(idpengguna) ON DELETE CASCADE;


-- Completed on 2025-10-20 14:44:44

--
-- PostgreSQL database dump complete
--

\unrestrict 908hqml0rnz4MflTP1utwOgrgteqkqtIXPkc76IKAn7Ez3ra9COHgVVQhV7kUZ1

