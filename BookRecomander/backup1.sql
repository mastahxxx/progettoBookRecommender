--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
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
-- Name: Consigli; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Consigli" (
    id_libro integer NOT NULL,
    id_codice_fiscale character(16) NOT NULL,
    id_libro_consigliato integer
);


ALTER TABLE public."Consigli" OWNER TO postgres;

--
-- Name: Librerie; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Librerie" (
    cod_libreria integer NOT NULL,
    nome_libreria character varying(30) NOT NULL,
    id_libro integer NOT NULL,
    id_codice_fiscale character(16) NOT NULL
);


ALTER TABLE public."Librerie" OWNER TO postgres;

--
-- Name: Librerie_cod_libreria_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public."Librerie" ALTER COLUMN cod_libreria ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."Librerie_cod_libreria_seq"
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1
);


--
-- Name: Libri; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Libri" (
    titolo character varying(300) NOT NULL,
    autore character varying(300),
    anno_pubblicazione character(4) NOT NULL,
    cod_libro integer NOT NULL
);


ALTER TABLE public."Libri" OWNER TO postgres;

--
-- Name: Libri_cod_libro_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public."Libri" ALTER COLUMN cod_libro ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."Libri_cod_libro_seq"
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1
);


--
-- Name: NoteValutazioni; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."NoteValutazioni" (
    nota_stile character varying,
    nota_contenuto character varying,
    nota_gradevolezza character varying,
    nota_originalita character varying,
    nota_edizione character varying,
    cf character(16),
    id_libro integer
);


ALTER TABLE public."NoteValutazioni" OWNER TO postgres;

--
-- Name: UtentiRegistrati; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."UtentiRegistrati" (
    nome character varying(30) NOT NULL,
    cognome character varying(30) NOT NULL,
    codice_fiscale character(16) NOT NULL,
    "userId" character varying(30) NOT NULL,
    email character(30) NOT NULL,
    password character varying(30) NOT NULL
);


ALTER TABLE public."UtentiRegistrati" OWNER TO postgres;

--
-- Name: Valutazioni; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Valutazioni" (
    id_libro integer NOT NULL,
    id_codice_fiscale character(16) NOT NULL,
    stile integer NOT NULL,
    contenuto integer NOT NULL,
    gradevolezza integer NOT NULL,
    "originalità" integer NOT NULL,
    edizione integer NOT NULL
);


ALTER TABLE public."Valutazioni" OWNER TO postgres;

--
-- Data for Name: Consigli; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Consigli" (id_libro, id_codice_fiscale, id_libro_consigliato) FROM stdin;
\.


--
-- Data for Name: Librerie; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Librerie" (cod_libreria, nome_libreria, id_libro, id_codice_fiscale) FROM stdin;
\.


--
-- Data for Name: Libri; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Libri" (titolo, autore, anno_pubblicazione, cod_libro) FROM stdin;
\.


--
-- Data for Name: NoteValutazioni; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."NoteValutazioni" (nota_stile, nota_contenuto, nota_gradevolezza, nota_originalita, nota_edizione, cf, id_libro) FROM stdin;
\.


--
-- Data for Name: UtentiRegistrati; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."UtentiRegistrati" (nome, cognome, codice_fiscale, "userId", email, password) FROM stdin;
andrea	rossi	bbbbb           	arossi	arossi@prova.it               	pluto
\.


--
-- Data for Name: Valutazioni; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Valutazioni" (id_libro, id_codice_fiscale, stile, contenuto, gradevolezza, "originalità", edizione) FROM stdin;
0	bbbbb           	6	8	5	5	3
\.


--
-- Name: Librerie_cod_libreria_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Librerie_cod_libreria_seq"', 0, false);


--
-- Name: Libri_cod_libro_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Libri_cod_libro_seq"', 0, false);


--
-- Name: Librerie PK_cod_libreria; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Librerie"
    ADD CONSTRAINT "PK_cod_libreria" PRIMARY KEY (cod_libreria);


--
-- Name: Valutazioni PK_id_libro_codice_fiscale; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Valutazioni"
    ADD CONSTRAINT "PK_id_libro_codice_fiscale" PRIMARY KEY (id_libro, id_codice_fiscale);


--
-- Name: Consigli PK_id_libro_id_codice_fiscale; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Consigli"
    ADD CONSTRAINT "PK_id_libro_id_codice_fiscale" PRIMARY KEY (id_libro, id_codice_fiscale);


--
-- Name: UtentiRegistrati UtentiRegistrati_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."UtentiRegistrati"
    ADD CONSTRAINT "UtentiRegistrati_pkey" PRIMARY KEY (codice_fiscale);


--
-- Name: Librerie FK_id_codice_fiscale; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Librerie"
    ADD CONSTRAINT "FK_id_codice_fiscale" FOREIGN KEY (id_codice_fiscale) REFERENCES public."UtentiRegistrati"(codice_fiscale) NOT VALID;


--
-- Name: Consigli FK_id_codice_fiscale; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Consigli"
    ADD CONSTRAINT "FK_id_codice_fiscale" FOREIGN KEY (id_codice_fiscale) REFERENCES public."UtentiRegistrati"(codice_fiscale) NOT VALID;


--
-- Name: Valutazioni FK_id_codice_fiscale; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Valutazioni"
    ADD CONSTRAINT "FK_id_codice_fiscale" FOREIGN KEY (id_codice_fiscale) REFERENCES public."UtentiRegistrati"(codice_fiscale) NOT VALID;


--
-- PostgreSQL database dump complete
--

