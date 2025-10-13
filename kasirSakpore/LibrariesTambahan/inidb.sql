--
-- PostgreSQL database dump
--

\restrict 3mBCfxvtFu4ZDy4N1eZNkHTVIUKeXe7EQVCtaS128kjHMMnLsOc6wOrFtItIvYM

-- Dumped from database version 13.22
-- Dumped by pg_dump version 13.22

-- Started on 2025-10-13 11:35:34

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
12	SKU12	Indomie Soto Mie	3500.00	17	Mie Instan	images/indomie.jpg	3000.00	0.00	pcs
13	SKU13	Indomie Kari Ayam	3600.00	78	Mie Instan	images/indomie.jpg	3000.00	0.00	pcs
15	SKU15	Indomie Ayam Bawang	3500.00	104	Mie Instan	images/indomie.jpg	3000.00	0.00	pcs
16	SKU16	Indomie Empal Gentong	4200.00	58	Mie Instan	images/indomie.jpg	3000.00	0.00	pcs
17	SKU17	Indomie Goreng Aceh	4500.00	38	Mie Instan	images/indomie.jpg	3000.00	0.00	pcs
18	SKU18	Indomie Iga Penyet	5000.00	27	Mie Instan	images/indomie.jpg	3000.00	0.00	pcs
19	SKU19	Indomie HypeAbis Seblak	5500.00	23	Mie Instan	images/indomie.jpg	3000.00	0.00	pcs
3	SKU003	Pulpen Pilot	5000.00	0	ATK	images/indomie.jpg	3000.00	0.00	pcs
2	SKU002	Aqua Botol 600ml	4000.00	88	Minuman	images/indomie.jpg	3000.00	0.00	pcs
4	SKU004	Kipas Angin Mini	30000.00	10	Elektronik	images/indomie.jpg	30000.00	0.00	pcs
1	SKU001	Indomie Goreng	3500.00	121	KELAZ	images/indomie.jpg	3500.00	0.00	pcs
5	aaaaaa	sssss	22222.00	17	KELAZ	images/indomie.jpg	3000.00	0.00	pcs
14	SKU14	Indomie Cabe Ijo	3800.00	64	Mie Instan	images/indomie.jpg	3000.00	0.00	pcs
20	123456789	SUsu	28000.00	0	ATK	\N	20000.00	40.00	pcs
10	SKU10	Indomie Goreng Original	3500.00	90	Mie Instan	images/indomie.jpg	3000.00	0.00	pcs
\.


--
-- TOC entry 3078 (class 0 OID 24586)
-- Dependencies: 200
-- Data for Name: barangmasuk; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.barangmasuk (idbarangmasuk, kodebarang, nama, satuan, jumlahmasuk, hargabarang, totalharga, tanggal, supplier, skubarang) FROM stdin;
1	1	Indomie Goreng	pcs	50	3000.00	150000.00	2025-09-01	PT Sumber Makmur	\N
2	2	Aqua Botol 600ml	botol	100	3500.00	350000.00	2025-09-02	CV Maju Jaya	\N
3	4	Kipas Angin Mini	pcs	20	30000.00	600000.00	2025-10-03	PT Sumber Makmur	\N
4	14	Indomie Cabe Ijo	pcs	12	3800.00	45600.00	2025-10-03	PT Sumber Makmur	\N
5	12	Indomie Soto Mie	pcs	2	3500.00	7000.00	2025-10-10	PT Sumber Makmur	\N
6	2	Aqua Botol 600ml	pcs	2	4000.00	8000.00	2025-10-03	PT Sumber Makmur	SKU002
7	1	Indomie Goreng	pcs	1	3500.00	3500.00	2025-10-03	PT Sumber Makmur	SKU001
8	1	Indomie Goreng	pcs	1	3500.00	3500.00	2025-10-03	PT Sumber Makmur	SKU001
9	1	Indomie Goreng	pcs	20	3500.00	70000.00	2025-10-06	PT Sumber Makmur	SKU001
10	2	Aqua Botol 600ml	pcs	1	3000.00	3000.00	2025-10-11	PT Sumber Makmur	SKU002
11	3	Pulpen Pilot	pcs'	1	3000.00	3000.00	2025-10-11	PT Sumber Makmur	SKU003
12	1	Indomie Goreng	pcs	50	3500.00	175000.00	2025-10-12	PT Sumber Makmur	SKU001
13	1	Indomie Goreng	pcs	50	3500.00	175000.00	2025-10-12	PT Sumber Makmur	SKU001
14	1	Indomie Goreng	pcs	50	3500.00	175000.00	2025-10-12	PT Sumber Makmur	SKU001
15	1	Indomie Goreng	pcs	2	3500.00	7000.00	2025-10-13	CV Maju Jaya	SKU001
\.


--
-- TOC entry 3080 (class 0 OID 24591)
-- Dependencies: 202
-- Data for Name: detailtransaksi; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.detailtransaksi (iddetailtransaksi, kodebarang, idtransaksi, namabarang, jumlah, harga, keterangan, subtotal) FROM stdin;
1	1	1	Indomie Goreng	2	3500.00	Pesanan reguler	7000.00
2	2	1	Aqua Botol 600ml	1	3000.00	Air mineral	3000.00
3	3	2	Pulpen Pilot	4	5000.00	ATK kantor	20000.00
4	11	3	Indomie Goreng Rendang	2	4000.00		8000.00
5	14	3	Indomie Cabe Ijo	2	3800.00		7600.00
6	13	3	Indomie Kari Ayam	2	3600.00		7200.00
7	16	4	Indomie Empal Gentong	2	4200.00		8400.00
8	17	4	Indomie Goreng Aceh	2	4500.00		9000.00
9	10	5	Indomie Goreng Original	3	3500.00	enak	10500.00
10	12	5	Indomie Soto Mie	2	3500.00	enak	7000.00
11	11	5	Indomie Goreng Rendang	2	4000.00	enak	8000.00
12	14	5	Indomie Cabe Ijo	2	3800.00	enak	7600.00
13	13	5	Indomie Kari Ayam	2	3600.00	enak	7200.00
14	11	6	Indomie Goreng Rendang	6	4000.00	ngutang nih	24000.00
15	13	6	Indomie Kari Ayam	6	3600.00	ngutang nih	21600.00
16	10	6	Indomie Goreng Original	7	3500.00	ngutang nih	24500.00
17	17	6	Indomie Goreng Aceh	8	4500.00	ngutang nih	36000.00
18	18	7	Indomie Iga Penyet	5	5000.00	---	25000.00
19	19	7	Indomie HypeAbis Seblak	5	5500.00	---	27500.00
20	1	7	Indomie Goreng	5	3500.00	---	17500.00
21	5	7	sssss	5	22222.00	---	111110.00
22	4	8	Kipas Angin Mini	9	75000.00		675000.00
23	18	9	Indomie Iga Penyet	8	5000.00		40000.00
24	15	10	Indomie Ayam Bawang	6	3500.00		21000.00
25	13	11	Indomie Kari Ayam	2	3600.00		7200.00
26	1	12	Indomie Goreng	2	3500.00		7000.00
27	17	12	Indomie Goreng Aceh	2	4500.00		9000.00
28	1	13	Indomie Goreng	1	3500.00		3500.00
29	1	14	Indomie Goreng	1	3500.00		3500.00
30	1	14	Indomie Goreng	1	3500.00		3500.00
31	1	14	Indomie Goreng	1	3500.00		3500.00
32	1	14	Indomie Goreng	1	3500.00		3500.00
33	1	15	Indomie Goreng	1	3500.00		3500.00
34	1	15	Indomie Goreng	1	3500.00		3500.00
35	1	15	Indomie Goreng	1	3500.00		3500.00
36	1	15	Indomie Goreng	1	3500.00		3500.00
37	1	15	Indomie Goreng	1	3500.00		3500.00
38	2	15	Aqua Botol 600ml	1	4000.00		4000.00
39	1	16	Indomie Goreng	1	3500.00		3500.00
40	1	16	Indomie Goreng	1	3500.00		3500.00
41	1	16	Indomie Goreng	1	3500.00		3500.00
42	1	16	Indomie Goreng	1	3500.00		3500.00
43	1	16	Indomie Goreng	1	3500.00		3500.00
44	3	17	Pulpen Pilot	50	5000.00		250000.00
45	1	18	Indomie Goreng	1	3500.00		3500.00
46	1	18	Indomie Goreng	1	3500.00		3500.00
47	1	18	Indomie Goreng	1	3500.00		3500.00
48	1	18	Indomie Goreng	1	3500.00		3500.00
49	2	18	Aqua Botol 600ml	1	4000.00		4000.00
50	1	18	Indomie Goreng	1	3500.00		3500.00
51	12	19	Indomie Soto Mie	100	3500.00		350000.00
52	1	20	Indomie Goreng	3	3500.00		10500.00
53	1	20	Indomie Goreng	1	3500.00		3500.00
54	2	20	Aqua Botol 600ml	1	4000.00		4000.00
55	3	20	Pulpen Pilot	1	5000.00		5000.00
56	2	20	Aqua Botol 600ml	1	4000.00		4000.00
57	1	21	Indomie Goreng	1	3500.00		3500.00
58	1	22	Indomie Goreng	100	3500.00		350000.00
59	2	22	Aqua Botol 600ml	100	4000.00		400000.00
60	1	23	Indomie Goreng	1	3500.00		3500.00
61	1	24	Indomie Goreng	1	3500.00		3500.00
62	1	25	Indomie Goreng	1	3500.00		3500.00
63	4	26	Kipas Angin Mini	1	30000.00		30000.00
64	1	27	Indomie Goreng	1	3500.00		3500.00
65	14	28	Indomie Cabe Ijo	2	3800.00		7600.00
\.


--
-- TOC entry 3082 (class 0 OID 24599)
-- Dependencies: 204
-- Data for Name: kategori; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.kategori (idkategori, namakategori) FROM stdin;
2	Minuman
4	Elektronik
3	ATK8
1	KELAZ
\.


--
-- TOC entry 3084 (class 0 OID 24604)
-- Dependencies: 206
-- Data for Name: keuangan; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.keuangan (idkeuangan, idasal, jeniskeuangan, masuk, keluar, tanggal) FROM stdin;
1	1	Penjualan	10000.00	0.00	2025-09-01
2	2	Pembelian Barang	0.00	350000.00	2025-09-02
3	11	Penjualan	500000.00	0.00	2025-09-01
4	12	Pembelian Barang	0.00	350000.00	2025-09-01
5	13	Penjualan	420000.00	0.00	2025-09-02
6	14	Pembelian Barang	0.00	300000.00	2025-09-02
7	15	Penjualan	600000.00	0.00	2025-09-03
8	16	Pembelian Barang	0.00	450000.00	2025-09-03
9	17	Penjualan	550000.00	0.00	2025-09-04
10	18	Pembelian Barang	0.00	400000.00	2025-09-04
11	19	Penjualan	480000.00	0.00	2025-09-05
12	20	Pembelian Barang	0.00	320000.00	2025-09-05
13	21	Penjualan	700000.00	0.00	2025-09-06
14	22	Pembelian Barang	0.00	500000.00	2025-09-06
15	23	Penjualan	530000.00	0.00	2025-09-07
16	24	Pembelian Barang	0.00	410000.00	2025-09-07
17	25	Penjualan	610000.00	0.00	2025-09-08
18	26	Pembelian Barang	0.00	460000.00	2025-09-08
19	27	Penjualan	750000.00	0.00	2025-09-09
20	28	Pembelian Barang	0.00	520000.00	2025-09-09
21	29	Penjualan	680000.00	0.00	2025-09-10
22	30	Pembelian Barang	0.00	470000.00	2025-09-10
23	31	Penjualan	720000.00	0.00	2025-09-11
24	32	Pembelian Barang	0.00	450000.00	2025-09-11
25	33	Penjualan	480000.00	0.00	2025-09-12
26	34	Pembelian Barang	0.00	330000.00	2025-09-12
27	35	Penjualan	810000.00	0.00	2025-09-13
28	36	Pembelian Barang	0.00	500000.00	2025-09-13
29	37	Penjualan	530000.00	0.00	2025-09-14
30	38	Pembelian Barang	0.00	400000.00	2025-09-14
31	39	Penjualan	900000.00	0.00	2025-09-15
32	40	Pembelian Barang	0.00	600000.00	2025-09-15
33	41	Penjualan	610000.00	0.00	2025-09-16
34	42	Pembelian Barang	0.00	420000.00	2025-09-16
35	43	Penjualan	850000.00	0.00	2025-09-17
36	44	Pembelian Barang	0.00	550000.00	2025-09-17
37	45	Penjualan	670000.00	0.00	2025-09-18
38	46	Pembelian Barang	0.00	430000.00	2025-09-18
39	47	Penjualan	780000.00	0.00	2025-09-19
41	49	Penjualan	950000.00	0.00	2025-09-20
43	3	Penjualan Barang	20800.00	0.00	2025-09-21
44	4	Penjualan Barang	17400.00	0.00	2025-09-21
45	5	Penjualan Barang	36800.00	0.00	2025-09-21
46	6	Penjualan Barang	106100.00	0.00	2025-09-21
47	7	Penjualan Barang	181110.00	0.00	2025-09-22
48	8	Penjualan Barang	450000.00	0.00	2025-09-06
49	9	Penjualan Barang	40000.00	0.00	2025-09-06
50	10	Penjualan Barang	21000.00	0.00	2025-10-01
51	11	Penjualan Barang	7200.00	0.00	2025-10-02
52	12	Penjualan Barang	16000.00	0.00	2025-10-18
40	48	Pembelian Barang	0.00	4000.00	2025-09-19
53	\N	SALDO AWAL	1000000.00	0.00	2025-10-01
54	\N	saldo awla	10.00	0.00	2025-10-01
55	\N	SALDO AWAL	200000.00	0.00	2025-10-03
56	3	Pembelian - Kipas Angin Mini	0.00	600000.00	2025-10-03
57	4	Pembelian - Indomie Cabe Ijo	0.00	45600.00	2025-10-03
58	5	Pembelian - Indomie Soto Mie	0.00	7000.00	2025-10-10
59	6	Pembelian - Aqua Botol 600ml	0.00	8000.00	2025-10-03
60	7	Pembelian - Indomie Goreng	0.00	3500.00	2025-10-03
61	8	Pembelian - Indomie Goreng	0.00	3500.00	2025-10-03
62	13	Penjualan Barang	3500.00	0.00	2025-10-05
63	14	Penjualan Barang	14000.00	0.00	2025-10-05
64	15	Penjualan Barang	21500.00	0.00	2025-10-05
65	16	Penjualan Barang	17500.00	0.00	2025-10-06
66	9	Pembelian - Indomie Goreng	0.00	70000.00	2025-10-06
67	17	Penjualan Barang	250000.00	0.00	2025-10-06
68	18	Penjualan Barang	21500.00	0.00	2025-10-06
69	10	Pembelian - Aqua Botol 600ml	0.00	3000.00	2025-10-11
70	11	Pembelian - Pulpen Pilot	0.00	3000.00	2025-10-11
71	19	Penjualan Barang	350000.00	0.00	2025-10-11
72	20	Penjualan Barang	27000.00	0.00	2025-10-12
73	21	Penjualan Barang	3500.00	0.00	2025-10-12
74	\N	SALDO AWAL	10000000.00	0.00	2025-10-12
75	12	Pembelian - Indomie Goreng	0.00	175000.00	2025-10-12
76	13	Pembelian - Indomie Goreng	0.00	175000.00	2025-10-12
77	14	Pembelian - Indomie Goreng	0.00	175000.00	2025-10-12
78	22	Penjualan Barang	354000.00	0.00	2025-10-12
79	23	Penjualan Barang	3500.00	0.00	2025-10-12
80	24	Penjualan Barang	3500.00	0.00	2025-10-12
81	25	Penjualan Barang	3500.00	0.00	2025-10-12
82	26	Penjualan Barang	30000.00	0.00	2025-10-12
83	27	Penjualan Barang	3500.00	0.00	2025-10-13
84	15	Pembelian - Indomie Goreng	0.00	7000.00	2025-10-13
85	28	Penjualan Barang	7600.00	0.00	2025-10-13
\.


--
-- TOC entry 3086 (class 0 OID 24611)
-- Dependencies: 208
-- Data for Name: metodepembayaran; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.metodepembayaran (idmetodepembayaran, namametodepembayaran) FROM stdin;
1	Cash
2	Transfer Bank
3	QRIS
\.


--
-- TOC entry 3088 (class 0 OID 24616)
-- Dependencies: 210
-- Data for Name: pengguna; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pengguna (idpengguna, username, password, role, status, nama) FROM stdin;
5	ayam	ayam	admin	aktif	\N
2	kasir1	kasir123	kasir	aktif	SIGMA
3	manager	manager123	manager	aktif	manager
1	admin	admin123	admin	aktif	admin
4	kasir2	kasir123	kasir	aktif	SIGMA
\.


--
-- TOC entry 3096 (class 0 OID 24821)
-- Dependencies: 218
-- Data for Name: riwayat_login; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.riwayat_login (id, idpengguna, username, nama_pemakai, waktu_login) FROM stdin;
1	2	kasir1	DANI JUNIANTO	2025-10-11 19:03:30.342006
2	2	kasir1	DANI JUNIANTO	2025-10-11 19:03:56.113382
3	2	kasir1	DANI JUNIANTO	2025-10-11 19:04:24.847646
4	2	kasir1	ANATA WA BANKYOU NIHONGGO	2025-10-11 19:08:26.046921
5	2	kasir1	ANATA WA BANKYOU NIHONGGO	2025-10-11 19:08:53.903868
6	1	admin	admin	2025-10-11 19:13:35.610111
7	1	admin	admin	2025-10-11 19:30:15.464192
8	1	admin	admin	2025-10-11 19:34:29.459694
9	1	admin	admin	2025-10-11 19:37:54.455769
13	3	manager	budi	2025-10-12 16:54:52.595766
17	1	admin	admin	2025-10-12 17:31:00.576421
18	1	admin	admin	2025-10-12 17:56:12.032983
19	3	manager	BUDI SANTOSO	2025-10-12 18:22:54.583799
20	3	manager	BUDI SANTOSO	2025-10-12 18:25:34.306651
21	1	admin	admin	2025-10-12 18:28:57.380829
22	1	admin	admin	2025-10-12 18:31:14.034495
23	1	admin	SIGMA	2025-10-12 18:51:56.442116
10	1	admin	admin	2025-10-12 18:58:23.447097
15	2	kasir1	SIGMA	2025-10-12 19:03:37.166274
11	1	admin	admin	2025-10-12 19:03:59.959993
12	3	manager	manager	2025-10-12 19:04:24.556149
14	1	admin	HAI	2025-10-12 20:04:31.388308
16	1	admin	HAI	2025-10-12 20:04:46.62573
24	1	admin	admin	2025-10-13 09:58:08.474053
25	4	kasir2	SIGMA	2025-10-13 09:59:00.023323
\.


--
-- TOC entry 3090 (class 0 OID 24622)
-- Dependencies: 212
-- Data for Name: supplier; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.supplier (idsupplier, namasupplier, notelp, status, alamat) FROM stdin;
1	PT Sumber Makmur	08123456789	aktif	Jl. Merdeka No.1
2	CV Maju Jaya	082233445566	aktif	Jl. Raya Selatan No.10
\.


--
-- TOC entry 3092 (class 0 OID 24631)
-- Dependencies: 214
-- Data for Name: transaksi; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.transaksi (idtransaksi, notransaksi, namapengguna, tgl_transaksi, subtotal, diskon, grand_total, metodepembayaran) FROM stdin;
1	TRX001	kasir1	2025-09-17 08:48:04.386779	10000.00	0.00	10000.00	Cash
2	TRX002	kasir1	2025-09-17 08:48:04.386779	20000.00	2000.00	18000.00	QRIS
3	aaaa	siti	2025-09-21 18:47:18.488	20800.00	0.00	20800.00	Tunai
4	T09211	Siti	2025-09-21 19:03:12.681	17400.00	0.00	17400.00	Kredit
5	T09212	---	2025-09-21 19:10:38.427	36800.00	0.00	36800.00	Kredit
6	T09213	siti	2025-09-21 19:19:29.96	106100.00	0.00	106100.00	Tunai
7	T09221	SITI	2025-09-22 06:36:36.849	181110.00	0.00	181110.00	Tunai
8	T09291		2025-09-06 11:56:00.995	450000.00	0.00	450000.00	Tunai
9	T09291		2025-09-06 11:56:00.995	40000.00	0.00	40000.00	Tunai
10	T10011		2025-10-01 05:37:43.975	21000.00	0.00	21000.00	Tunai
11	T10012		2025-10-02 05:47:02.804	7200.00	0.00	7200.00	Tunai
12	T10012		2025-10-18 05:48:13.574	16000.00	0.00	16000.00	Tunai
13	T10051	KASIR	2025-10-05 20:50:38.355	3500.00	0.00	3500.00	Tunai
14	T10052	KASIR	2025-10-05 21:35:15.718	14000.00	0.00	14000.00	Tunai
15	T10053	K	2025-10-05 21:51:53.293	21500.00	0.00	21500.00	Tunai
16	T10061		2025-10-06 06:55:08.871	17500.00	0.00	17500.00	Tunai
17	T10062	s	2025-10-06 07:06:55.463	250000.00	0.00	250000.00	Tunai
18	T10063	admin	2025-10-06 14:41:52.215	21500.00	0.00	21500.00	Tunai
19	T10111	admin	2025-10-11 13:58:55.898	350000.00	0.00	350000.00	Tunai
20	T10121	admin	2025-10-12 14:49:21.628	27000.00	0.00	27000.00	Tunai
21	T10122	ANATA WA BANKYOU NIHONGGO	2025-10-12 17:26:44.404	3500.00	0.00	3500.00	Tunai
22	T10123	admin	2025-10-12 17:32:57.394	354000.00	0.00	354000.00	Tunai
23	T10124	SIGMA	2025-10-12 18:51:59.551	3500.00	0.00	3500.00	Tunai
24	T10125	SIGMA	2025-10-12 18:56:03.139	3500.00	0.00	3500.00	Tunai
25	T10126	SIGMA	2025-10-12 18:57:52.554	3500.00	0.00	3500.00	Tunai
26	T10127	SIGMA	2025-10-12 19:03:40.107	30000.00	0.00	30000.00	Tunai
27	T10131	suki	2025-10-13 01:21:02.335	3500.00	0.00	3500.00	Tunai
28	T10132	SIGMA	2025-10-13 09:57:06.824	7600.00	0.00	7600.00	Tunai
\.


--
-- TOC entry 3111 (class 0 OID 0)
-- Dependencies: 201
-- Name: barangmasuk_idbarangmasuk_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.barangmasuk_idbarangmasuk_seq', 15, true);


--
-- TOC entry 3112 (class 0 OID 0)
-- Dependencies: 203
-- Name: detailtransaksi_iddetailtransaksi_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.detailtransaksi_iddetailtransaksi_seq', 65, true);


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

SELECT pg_catalog.setval('public.keuangan_idkeuangan_seq', 85, true);


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

SELECT pg_catalog.setval('public.riwayat_login_id_seq', 25, true);


--
-- TOC entry 3118 (class 0 OID 0)
-- Dependencies: 213
-- Name: supplier_idsupplier_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.supplier_idsupplier_seq', 2, true);


--
-- TOC entry 3119 (class 0 OID 0)
-- Dependencies: 215
-- Name: transaksi_idtransaksi_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.transaksi_idtransaksi_seq', 28, true);


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


-- Completed on 2025-10-13 11:35:34

--
-- PostgreSQL database dump complete
--

\unrestrict 3mBCfxvtFu4ZDy4N1eZNkHTVIUKeXe7EQVCtaS128kjHMMnLsOc6wOrFtItIvYM

