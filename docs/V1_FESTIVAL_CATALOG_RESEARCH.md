# V1 Festival Catalog Research — Data Quality First

**Research date:** 13 September 2026  
**Window:** 13 September 2026 → 13 September 2027  
**Genres:** Drum & Bass, House, Techno  
**Regions:** Europe (primary), Southeast Asia (Thailand / Indonesia where data is usable), Japan (boutique + a few dated 2026/27 shells), South Asia (only where data is usable)  
**Question this document answers:** *Can Om realistically maintain this festival in Festival Lineup Tracker without scraping?*

This is **not** a “best electronic festivals” list. Fame is a penalty when it means 15 stages, Instagram-only updates, or city-wide chaos.

**Legal note:** Use official pages as a **reference for manual curation**. Do not scrape. Do not treat RA / Festalgia / Frontstage / BookMyShow scrapers as production sources. This is not legal advice.

---

## 1. Executive summary

### What the data landscape actually looks like on 13 Sep 2026

| Reality | Implication |
|---|---|
| Almost **no** 2027 outdoor festival has published a full **stage + start + end** grid yet | Do **not** expect a `SCHEDULE_VERIFIED` catalog today |
| Official **timetables typically appear 7–21 days before** (Let It Roll even says ~14 days) | V1 Clash demo = pick fests that **historically publish official HTML/PDF grids**, then transcribe when they drop |
| **Netherlands / Belgium / Czech indoor-outdoor circuit** has the clearest official websites, dates, and later timetables | Best region for maintainability |
| **Tisno, Croatia (The Garden)** cluster (Hospitality, historically Defected/Dimensions) has bounded sites (3–5 stages) | Excellent clash shape; 2026 editions of Defected/Dimensions are **already over** |
| **UK mega dance fests** (Creamfields) and **city-wide ADE** are famous and a maintenance trap | Reject or identity-only |
| **South Asia has almost no clash-grade official timetables** | Keep **3** India names, mostly `LINEUP_TBA`. Do **not** pad to 10 |
| **Southeast Asia (esp. Thailand + Jakarta/Bali) has better 2026 official sites than India** | Keep **~5–7** SEA names. Do **not** pad with Full Moon parties, club nights, or unannounced 2027 editions |
| **Japan’s best dance fests (rural, Rainbow Disco Club) already ran in 2026; 2027 dates are missing** | Keep **~3–4** JP names in-window. Do **not** invent a Japan top-10 |

### Best countries for *our* data (not for tourism)

1. **Netherlands** — Awakenings, DGTL, Dekmantel, Liquicity, Soenda, Loveland, Verknipt: English sites, confirmed 2027 dates, later HTML programs.  
2. **Czechia** — Let It Roll (summer + winter): official site, named stages, **documented timetable policy**.  
3. **Belgium** — Rampage Weekend, Extrema Outdoor: official dates, indoor/outdoor bounded.  
4. **Italy** — Kappa FuturFestival: excellent historical HTML day-lineups; 2027 dates live.  
5. **Germany** — Time Warp Mannheim: one night, 5 floors, historically exact times; 3 Apr 2027 confirmed.  
6. **Croatia (Tisno / Pag)** — Hospitality on the Beach 2027, Hideout 2027, Outlook 2027, Verknipt Croatia 2027: holiday format, 4–5 stages, official ticket sites.  
7. **Poland** — Unsound 2026 is the **only researched fest with a live official HTML schedule today** — but genre fit is experimental, not D&B/house/techno-first.  
8. **Thailand** — strongest SEA data this window: **808 Festival** (Oct 2026, 2 stages, lineup live), **EDC Thailand** (Dec 2026, 6 named stages + official artist list), **Tomorrowland Thailand** (Dec 2026, official hours + 100+ names, clash-hostile), **Wonderfruit** (Dec 2026, dates/gates official, timetable later on app).  
9. **Indonesia** — **DWP 2026** (Jakarta, phase-one lineup on official ticket site), **Dekmantel at Potato Head Bali** (26 Sep 2026, house/techno, 2 rooms). Day Zero Bali **2027 dates not published**.  
10. **India** — Sunburn 2026 (headliners), Echoes of Earth 2026 (artist list on official site), Lollapalooza India 2027 (lineup in press). **Times: not public.** Ticketing often BookMyShow (aggregator).  
11. **Japan** — **best taste, awkward calendar.** Labyrinth (Oct 2026) has an excellent official English site + music hours + week-of timetable policy, but lineup is late/secret and historically **one floor** (Clash-weak). Ultra Japan (19–20 Sep 2026) has **3 named stages + an official running-order image** — rare — but it is **this weekend** and EDM-first. Snow Machine Hakuba Mar 2027 has a real D&B/house lineup on the official site; ski-village format is HARD. GMO SONIC Apr 2027 is dates-only. **rural 2026 and Rainbow Disco Club 2026 already happened; 2027 not official.**  
12. **Rest of SEA (SG / MY / PH / VN / KH / LA / MM)** — no second multi-day electronic festival with a maintainable 2026/27 official catalog found (ZoukOut dormant, Ultra SG/PH are old editions, We The Fest is indie/multi-genre).  
13. **Pakistan / Bangladesh / Nepal** — no multi-day electronic festival with maintainable official catalog data found for this window.  
14. **Sri Lanka** — one-nighters (YAGA, Wonderland, Sunfest) — **events, not festivals** for our model.

### Honest ratio for V1

**Recommended: ~24 Europe + ~5–7 Southeast Asia + ~3–4 Japan + ~3 India**  
Not 25+10 SA, not 10 SEA, not 10 JP. Japan is **not** empty — it is **under-dated** for 2027.

### Status vocabulary used below

As of **today**, almost every 2027 fest is `IDENTITY_ONLY` or `LINEUP_TBA`.  
`SCHEDULE_VERIFIED` is reserved for **official** stage + start/end **now**. Unofficial aggregator grids do **not** qualify.

**Clash potential /5** = how likely this fest is to *become* clash-ready from an official timetable Om can type, not whether times exist today.

---

## 2. How to read scores

| Score | Lineup completeness | Stages | Exact times | Source reliability | Update likelihood | Clash engine |
|---|---|---|---|---|---|---|
| 5 | Full official artist list | Named stages on official site | Official start **and** end | Organizer site / official PDF | Stable annual site + emailable org | 3–6 stages, bounded site, times will be official |
| 3 | Partial / headliners | “Several stages” but unnamed | Door times only | Mix of official + press | Seasonal Instagram | Usable if timetable drops |
| 1 | Rumour / aggregator AI | Unknown | None | Social only | Chaotic | Mega / city-wide / 10+ stages |

**Maintainability:** EASY = 1–3 days, ≤4 stages, one official URL. HARD = 6+ days, 6+ stages, multi-venue city, or Instagram-first.

---

## 3. Ranked V1 candidates (best ~36)

Sorted for **data quality + genre + maintainability + 2026/27 window**, not fame.

### Rank 1 — Let It Roll Winter 2027 (Czechia)

| | |
|---|---|
| **Basic** | Prague, Křižík Pavilions. **22–23 Jan 2027**. Pure **DnB**. Official: [letitroll.eu/event/let-it-roll-winter-2027](https://letitroll.eu/event/let-it-roll-winter-2027/) · Info: [letitroll.eu/info](https://letitroll.eu/info/) |
| **Timezone** | `Europe/Prague` |
| **Confirmed / lineup / stages / times** | Edition confirmed, tickets on sale. **3 stages** stated. **40+ artists** promised; Black Sun Empire named. **Timetable: official FAQ says ~14 days before.** Music 20:00–06:00 both nights. Historical winter pages show **per-set start–end HTML**. |
| **Status today** | `LINEUP_TBA` (partial names) → will become `SCHEDULE_VERIFIED` if they keep the 14-day PDF/HTML habit |
| **Scores** | Lineup 2 · Stages 4 · Times 2 (today) / 5 (expected) · Source 5 · Updates 4 · **Clash 5** |
| **Genre** | DnB **HIGH** · House LOW · Techno LOW |
| **Size / maintain** | Medium indoor · **EASY** |
| **Source** | Official festival site (not aggregator) |
| **Rec** | **TIER A** — first Clash demo |

### Rank 2 — Time Warp Germany 2027 (Mannheim)

| | |
|---|---|
| **Basic** | Maimarkthalle, Mannheim. **3 Apr 2027**. Techno institution. [time-warp.de](https://www.time-warp.de/) · tickets: [time-warp.de/tickets](https://www.time-warp.de/tickets/) |
| **Timezone** | `Europe/Berlin` |
| **Data** | Date confirmed on official homepage (“19 Hours. 5 Floors”). **2027 lineup not out.** 2026 edition already ran (21 Mar 2026) with the same format — **historical** times/floors were official. |
| **Status** | `IDENTITY_ONLY` |
| **Scores** | Lineup 1 · Stages 4 (5 floors known format) · Times 1 today / 5 expected · Source 5 · Updates 4 · **Clash 5** |
| **Genre** | DnB LOW · House MEDIUM · Techno **HIGH** |
| **Size / maintain** | Large indoor, **one night** · **EASY–MEDIUM** |
| **Rec** | **TIER A** |

### Rank 3 — Soenda Festival 2027 (Netherlands)

| | |
|---|---|
| **Basic** | Utrecht / Ruigenhoekse Polder. **Sat 29 May 2027, 12:00–23:00**. Techno / tech-house. Official: [soenda.net](https://www.soenda.net/) · [die-hard 2027 post](https://www.soenda.net/news/die-hard-ticket-sales-soenda-festival-2027/) |
| **Timezone** | `Europe/Amsterdam` |
| **Data** | Date + hours official. Lineup TBA. One-day outdoor, several stages (incl. bunker stages historically). |
| **Status** | `IDENTITY_ONLY` |
| **Scores** | Lineup 1 · Stages 3 · Times 1/4 expected · Source 5 · Updates 5 · **Clash 5** |
| **Genre** | DnB LOW · House **HIGH** · Techno **HIGH** |
| **Size / maintain** | Medium, **one day** · **EASY** |
| **Rec** | **TIER A** — best “small clash graph” |

### Rank 4 — DGTL Amsterdam 2027

| | |
|---|---|
| **Basic** | NDSM Docklands, Amsterdam. **26–28 Mar 2027**. House/techno. [dgtl-festival.com/en/dgtl-amsterdam](https://dgtl-festival.com/en/dgtl-amsterdam/) |
| **Timezone** | `Europe/Amsterdam` |
| **Data** | Official day/night **session hours** (e.g. Fri 22:00–05:00, Sat 12:00–23:00, etc.). Lineup TBA. Same site, named industrial venue. Tickets later 2026. |
| **Status** | `IDENTITY_ONLY` (hours = door times, not per-artist) |
| **Scores** | Lineup 1 · Stages 3 · Times 2 (blocks) / 4 expected · Source 5 · Updates 4 · Clash 4 |
| **Genre** | DnB LOW · House **HIGH** · Techno **HIGH** |
| **Size / maintain** | Large · **MEDIUM** |
| **Rec** | **TIER A** |

### Rank 5 — Liquicity Festival 2027 (Netherlands)

| | |
|---|---|
| **Basic** | Geestmerambacht / Noord-Scharwoude. **23–25 Jul 2027**. Liquid DnB. Official: [festival.liquicity.com](https://festival.liquicity.com/) |
| **Timezone** | `Europe/Amsterdam` |
| **Data** | 2027 site exists; homepage still thanking 2026 attendees. Dates corroborated by NL festival listings. **No 2027 lineup.** Historically **3 stages**, official artist pages, timetable before the weekend. |
| **Status** | `IDENTITY_ONLY` |
| **Scores** | Lineup 1 · Stages 4 (historical 3) · Times 1/5 expected · Source 4 · Updates 4 · **Clash 5** |
| **Genre** | DnB **HIGH** · House LOW · Techno LOW |
| **Size / maintain** | Medium · **EASY** |
| **Rec** | **TIER A** |

### Rank 6 — Kappa FuturFestival 2027 (Italy)

| | |
|---|---|
| **Basic** | Parco Dora, Turin. **2–4 Jul 2027**. House/techno (+ guests). [kappafuturfestival.it/en](https://www.kappafuturfestival.it/en) · program: [kappafuturfestival.it/en/program](https://www.kappafuturfestival.it/en/program) |
| **Timezone** | `Europe/Rome` |
| **Data** | 2027 dates + tickets official. Hours noon–midnight. **Program page still shows KFF 2026 day lineups** (Four Tet, Solomun, etc.) — treat as **historical quality proof**, not 2027 data. HTML by day is transcribable. |
| **Status** | `IDENTITY_ONLY` (2027) |
| **Scores** | Lineup 1 (2027) / 5 (2026 hist.) · Stages 3 · Times 1/4 · Source 5 · Updates 4 · Clash 4 |
| **Genre** | DnB LOW · House **HIGH** · Techno **HIGH** |
| **Size / maintain** | Large, 3 days · **MEDIUM** |
| **Rec** | **TIER A** |

### Rank 7 — Rampage Weekend 2027 (Belgium)

| | |
|---|---|
| **Basic** | AFAS Dome, Antwerp. **5–7 Mar 2027** (music 5–6 Mar per news). DnB + dubstep. [rampage.eu/events/rampage-weekend-2027](https://www.rampage.eu/events/rampage-weekend-2027) · [dates news](https://www.rampage.eu/news/rampage-weekend-2027-dates-announced) |
| **Timezone** | `Europe/Brussels` |
| **Data** | Official dates. Lineup TBA. Indoor stadium, multiple areas historically. |
| **Status** | `IDENTITY_ONLY` |
| **Scores** | Lineup 1 · Stages 3 · Times 1/4 · Source 5 · Updates 4 · Clash 4 |
| **Genre** | DnB **HIGH** · House LOW · Techno LOW |
| **Size / maintain** | Large indoor · **MEDIUM** |
| **Rec** | **TIER A** |

### Rank 8 — Hospitality on the Beach 2027 (Croatia)

| | |
|---|---|
| **Basic** | The Garden, Tisno. **30 Jun – 5 Jul 2027**. DnB holiday. [tickets.hospitalityonthebeach.com](https://tickets.hospitalityonthebeach.com/hospitality) |
| **Timezone** | `Europe/Zagreb` |
| **Data** | Official ticket site: 5 days, **five stages**, 150+ artists (format, not 2027 names). Tickets on sale. Lineup not dropped. Bounded resort = clash-friendly. |
| **Status** | `IDENTITY_ONLY` |
| **Scores** | Lineup 1 · Stages 4 · Times 1/5 expected · Source 5 · Updates 4 · **Clash 5** |
| **Genre** | DnB **HIGH** · House LOW · Techno LOW |
| **Size / maintain** | Medium-large · **MEDIUM** (5 days) |
| **Rec** | **TIER A** |

### Rank 9 — Awakenings Festival 2027 (Netherlands)

| | |
|---|---|
| **Basic** | Hilvarenbeek / Beekse Bergen. **9–11 Jul 2027**. Techno. [awakenings.com event](https://www.awakenings.com/en/events/2027/07/awakenings-festival-2027/399344/) |
| **Timezone** | `Europe/Amsterdam` |
| **Data** | Official dates. Day hours listed 15:00–23:00 on event page (verify camping nights). **No 2027 lineup.** Strong org, app, named stages historically. |
| **Status** | `IDENTITY_ONLY` |
| **Scores** | Lineup 1 · Stages 3 · Times 1/4 · Source 5 · Updates 4 · Clash 4 |
| **Genre** | DnB LOW · House MEDIUM · Techno **HIGH** |
| **Size / maintain** | Large · **MEDIUM** |
| **Rec** | **TIER A** |

### Rank 10 — Awakenings UpClose 2027 (Netherlands)

| | |
|---|---|
| **Basic** | Houtrak, Spaarnwoude. **15–16 May 2027**. Intimate techno. [awakenings.com](https://www.awakenings.com/en/) |
| **Timezone** | `Europe/Amsterdam` |
| **Data** | Official dates. Smaller than summer festival → easier clash graph. Lineup TBA. |
| **Status** | `IDENTITY_ONLY` |
| **Scores** | Lineup 1 · Stages 3 · Times 1/4 · Source 5 · Updates 4 · **Clash 5** |
| **Genre** | Techno **HIGH** · House MEDIUM · DnB LOW |
| **Size / maintain** | Medium/boutique · **EASY** |
| **Rec** | **TIER A** |

### Rank 11 — Extrema Outdoor 2027 (Belgium)

| | |
|---|---|
| **Basic** | Houthalen-Helchteren. **14–16 May 2027**. House/techno. [extrema.be/en](https://www.extrema.be/en/) |
| **Timezone** | `Europe/Brussels` |
| **Data** | Official 2027 dates on homepage. Lineup TBA. Multi-day forest site; historically publishes stage lineups. |
| **Status** | `IDENTITY_ONLY` |
| **Scores** | Lineup 1 · Stages 3 · Times 1/4 · Source 5 · Updates 4 · Clash 4 |
| **Genre** | DnB LOW · House **HIGH** · Techno **HIGH** |
| **Size / maintain** | Large · **MEDIUM** |
| **Rec** | **TIER A** |

### Rank 12 — Let It Roll 2027 (summer, Czechia)

| | |
|---|---|
| **Basic** | Jezero Most (Lake Most). **5–8 Aug 2027**. World’s largest DnB fest. [letitroll.eu/event/let-it-roll-2027](https://letitroll.eu/event/let-it-roll-2027/) |
| **Timezone** | `Europe/Prague` |
| **Data** | Official signup page; AllEvents lists 5–8 Aug (cross-check when they post full info). **50+ DJs, 6+ stages** (brand stats). Timetable historically late. **Harder than Winter.** |
| **Status** | `IDENTITY_ONLY` |
| **Scores** | Lineup 1 · Stages 3 · Times 1/4 · Source 4 · Updates 3 · Clash 3 |
| **Genre** | DnB **HIGH** |
| **Size / maintain** | Large · **HARD** (volume) |
| **Rec** | **TIER A as LINEUP_TBA**; complete schedule only if you have time after Winter |

### Rank 13 — Hospitality Weekender 2027 (UK)

| | |
|---|---|
| **Basic** | Bognor Regis. **12–15 Mar 2027**. DnB weekender. Third-party listings name Andy C; **verify on Hospitality/Hospitality Events official** before ingest. |
| **Timezone** | `Europe/London` |
| **Data** | Dates widely listed; lineup barely started. Indoor/holiday village format historically has **room/stage names + later timetable**. |
| **Status** | `IDENTITY_ONLY` |
| **Scores** | Lineup 1 · Stages 3 · Times 1/4 · Source 3 until official URL locked · Updates 3 · Clash 4 |
| **Genre** | DnB **HIGH** |
| **Size / maintain** | Medium · **MEDIUM** |
| **Rec** | **TIER B** until official 2027 hub is bookmarked |

### Rank 14 — Verknipt Festival 2027 (Netherlands)

| | |
|---|---|
| **Basic** | Strijkviertel, Utrecht. **5–6 Jun 2027**, 12:00/13:00–23:00. Hard techno. [verknipt.org](https://www.verknipt.org/) |
| **Timezone** | `Europe/Amsterdam` |
| **Data** | Official 2027 date + hours. Lineup TBA. Two days, one site. |
| **Status** | `IDENTITY_ONLY` |
| **Scores** | Lineup 1 · Stages 3 · Times 2 (hours) / 4 expected · Source 5 · Updates 5 · Clash 4 |
| **Genre** | Techno **HIGH** (hard) · House LOW · DnB LOW |
| **Size / maintain** | Medium · **EASY** |
| **Rec** | **TIER A** (techno mix; not house/DnB) |

### Rank 15 — Loveland Festival 2027 (Netherlands)

| | |
|---|---|
| **Basic** | Sloterpark, Amsterdam. **7–8 Aug 2027** (FAQ also mentions 8–9 in places — **confirm on loveland.nl before ingest**). House. [loveland.nl/festival/faq](https://loveland.nl/festival/faq/) |
| **Timezone** | `Europe/Amsterdam` |
| **Data** | Official FAQ, 18+, hours ~12:00–23:00. Lineup TBA. Two days, park site. |
| **Status** | `IDENTITY_ONLY` |
| **Scores** | Lineup 1 · Stages 3 · Times 2/4 · Source 4 · Updates 4 · Clash 4 |
| **Genre** | House **HIGH** · Techno MEDIUM · DnB LOW |
| **Size / maintain** | Large · **MEDIUM** |
| **Rec** | **TIER A** after date-hours reconcile |

### Rank 16 — Dekmantel Festival 2027 (Netherlands)

| | |
|---|---|
| **Basic** | Amsterdamse Bos + city venues. **28 Jul – 1 Aug 2027** (pre-reg / I amsterdam). House/techno/experimental. [dekmantelfestival.com](https://dekmantelfestival.com/faq) still documents **2026** program quality. |
| **Timezone** | `Europe/Amsterdam` |
| **Data** | 2027 dates in pre-reg. **Multi-venue** (Bos, Oude Kerk, Melkweg, Paradiso, night clubs) = HARD. Historical **HTML program + app** is excellent — but Om cannot treat 2026 artists as 2027. |
| **Status** | `IDENTITY_ONLY` |
| **Scores** | Lineup 1 · Stages 2 (many venues) · Times 1/4 · Source 5 · Updates 3 · Clash 3 |
| **Genre** | House **HIGH** · Techno **HIGH** · DnB LOW |
| **Size / maintain** | Large · **HARD** |
| **Rec** | **TIER B** — Fit yes; Clash only for Amsterdamse Bos days if you skip city nights |

### Rank 17 — Awakenings ADE 2026 (Netherlands) — **in window, data NOW**

| | |
|---|---|
| **Basic** | SugarFactory, Halfweg. **21–25 Oct 2026**. Eight named events (Drumcode, Metamorfosi, Adriatique ANL, etc.). Official hub: [awakenings.com](https://www.awakenings.com/en/) |
| **Timezone** | `Europe/Amsterdam` |
| **Data** | **Best near-term techno LINEUP.** Event-level hours exist; **per-DJ start/end often still missing** until closer to date. One venue, eight tickets — model as **one festival edition with 8 “stages/nights”** or 8 mini-events. |
| **Status** | `LINEUP_TBA` (artists named; not full clash grid) |
| **Scores** | Lineup 4 · Stages 3 · Times 2 · Source 5 · Updates 4 · Clash 3 |
| **Genre** | Techno **HIGH** · House MEDIUM |
| **Size / maintain** | Large week · **MEDIUM** |
| **Rec** | **TIER A for Fit / Oct demo**; Clash only if they publish set times |

### Rank 18 — DGTL ADE 2026

| | |
|---|---|
| **Basic** | NDSM Warehouse + Kromhouthal. **22–25 Oct 2026**. 8 events, 2 venues. [dgtl-festival.com DGTL ADE 2026](https://dgtl-festival.com/en/dgtl-ade/dgtl-ade-2026/) |
| **Timezone** | `Europe/Amsterdam` |
| **Data** | Official concept (house, techno, eurodance kickoff, Generator stage debut). Full artist grid: check event subpages before ingest. |
| **Status** | `LINEUP_TBA` / `IDENTITY_ONLY` depending on subpage depth |
| **Scores** | Lineup 3 · Stages 3 · Times 2 · Source 5 · Updates 4 · Clash 3 |
| **Genre** | House **HIGH** · Techno **HIGH** |
| **Size / maintain** | Medium week · **MEDIUM** |
| **Rec** | **TIER B** (don’t also ingest all of ADE city) |

### Rank 19 — Unsound 2026 (Poland) — **official schedule exists TODAY**

| | |
|---|---|
| **Basic** | Warsaw **2–6 Oct 2026**, Kraków **8–11 Oct 2026**. Experimental / club / some techno. [unsound.pl schedule](https://www.unsound.pl/en/unsound-2026/Schedule) |
| **Timezone** | `Europe/Warsaw` |
| **Data** | Official HTML schedule with **start times + venues**. Many days still empty (rolling). **Not** a D&B/house/techno festival; poor genre match. Two cities = two festivals in our DB or one HARD edition. |
| **Status** | Partial `SCHEDULE_VERIFIED` (start times; ends often missing) |
| **Scores** | Lineup 4 · Stages 4 (venues) · Times 3 · Source 5 · Updates 3 · Clash 2 (genre) |
| **Genre** | Techno MEDIUM · House LOW · DnB LOW |
| **Size / maintain** | Boutique/arts · **HARD** |
| **Rec** | **TIER C** unless you want a **schedule-pipeline test** in October |

### Rank 20 — Hideout Festival 2027 (Croatia)

| | |
|---|---|
| **Basic** | Zrće Beach, Pag. **29 Jun – 2 Jul 2027**. House/techno beach. [hideoutfestival.com](https://hideoutfestival.com/) |
| **Timezone** | `Europe/Zagreb` |
| **Data** | Official site selling 2027 packages; lineup later. Multi-venue beach (HARDER than Tisno Garden). |
| **Status** | `IDENTITY_ONLY` |
| **Scores** | Lineup 1 · Stages 2 · Times 1/3 · Source 4 · Updates 3 · Clash 3 |
| **Genre** | House **HIGH** · Techno MEDIUM |
| **Size / maintain** | Large · **HARD** (boats, clubs, beach) |
| **Rec** | **TIER B** — Fit; Clash optional |

### Rank 21 — Outlook Festival 2027 (Croatia)

| | |
|---|---|
| **Basic** | Tisno / The Garden cluster. Listings: **22–26 Jul 2027** (confirm on [outlookfestival.com](https://outlookfestival.com/) when 2027 page is the live edition). Bass / DnB / soundsystem. |
| **Timezone** | `Europe/Zagreb` |
| **Data** | Recurring Tisno; historically good stage names. **Verify 2027 official dates** before publish. |
| **Status** | `IDENTITY_ONLY` |
| **Scores** | Lineup 1 · Stages 3 · Times 1/4 · Source 3 until official 2027 locked · Clash 4 |
| **Genre** | DnB **HIGH** · House LOW · Techno LOW |
| **Size / maintain** | Medium · **MEDIUM** |
| **Rec** | **TIER B** after official 2027 URL |

### Rank 22 — Verknipt Croatia 2027

| | |
|---|---|
| **Basic** | Zrće / Novalja. **11–15 Jul 2027** + 10 Jul welcome. Hard techno holiday. [verkniptcroatia.org](https://verkniptcroatia.org/) |
| **Timezone** | `Europe/Zagreb` |
| **Data** | Official dates. Lineup page claimed; verify depth. Boats/pool = extra “stages.” |
| **Status** | `IDENTITY_ONLY` |
| **Scores** | Lineup 2 · Stages 2 · Times 1/3 · Source 4 · Clash 3 |
| **Genre** | Techno **HIGH** |
| **Size / maintain** | Medium · **HARD** |
| **Rec** | **TIER B** |

### Rank 23 — Sónar Barcelona 2027

| | |
|---|---|
| **Basic** | Barcelona. Listings **17–19 Jun 2027**. Electronic + by-day/by-night. Confirm [sonar.es](https://sonar.es/). |
| **Timezone** | `Europe/Madrid` |
| **Data** | Historically **excellent official program** (the gold standard HTML). 2027 lineup not out. Multi-venue **HARD**. |
| **Status** | `IDENTITY_ONLY` |
| **Scores** | Lineup 1 · Stages 3 hist. · Times 1/5 hist. · Source 5 hist. · Clash 3 |
| **Genre** | House **HIGH** · Techno **HIGH** · DnB LOW |
| **Size / maintain** | Mega/large · **HARD** |
| **Rec** | **TIER B** — add if you want one “program-quality” Spanish fest; not first 10 clash |

### Rank 24 — Creamfields 2027 (UK)

| | |
|---|---|
| **Basic** | Daresbury, Cheshire. Press: **26–29 Aug 2027**; **Prodigy** Saturday Apex — **dates not fully consistent across UK press**. Official: creamfields.com |
| **Timezone** | `Europe/London` |
| **Data** | Mega, many stages, DnB tents historically. **Terrible V1 clash** (volume). Fit: mixed EDM. |
| **Status** | `IDENTITY_ONLY` / partial lineup |
| **Scores** | Lineup 2 · Stages 2 · Times 1/2 · Source 3 · Clash **1** |
| **Genre** | House **HIGH** · DnB **HIGH** · Techno MEDIUM |
| **Size / maintain** | Mega · **HARD** |
| **Rec** | **TIER C / almost REJECT for Clash**; Fit-only if space |

### Rank 25 — Boom Festival 2027 (Portugal)

| | |
|---|---|
| **Basic** | Boomland, Idanha-a-Nova. **18–25 Jul 2027**. Psytrance-first; Alchemy Circle etc. can include techno. [boomfestival.org](https://www.boomfestival.org/news/30-years-boom) |
| **Timezone** | `Europe/Lisbon` |
| **Data** | Dates official. **Sells without lineup.** 8 days, many areas. **Genre fit is weak for V1 D&B/house/techno brief.** Already in *your* mental model of the app. |
| **Status** | `IDENTITY_ONLY` |
| **Scores** | Lineup 1 · Stages 3 (named areas hist.) · Times 1/3 · Source 5 · Clash 2 |
| **Genre** | Techno LOW–MEDIUM · House LOW · DnB LOW |
| **Size / maintain** | Large, 8 days · **HARD** |
| **Rec** | **TIER C** for *this* genre V1; optional identity for product continuity |

### Rank 26 — Sunburn Festival 2026 (India) — **in window**

| | |
|---|---|
| **Basic** | Mahalaxmi Racecourse, Mumbai. **18–19 Dec 2026** + closing **20 Dec NSCI Dome**. Official: [sunburn.in](https://sunburn.in/the-19th-edition-of-sunburn-festival-moves-to-mahalaxmi-racecourse-with-a-reimagined-two-day-format/) |
| **Timezone** | `Asia/Kolkata` |
| **Data** | Dates/venue official. Headliners in press/Instagram: **Chainsmokers, Sebastian Ingrosso, DJ Snake** (19 Dec). **No stages grid, no times.** Tickets: BookMyShow (aggregator). House/EDM more than techno/DnB. |
| **Status** | `LINEUP_TBA` (headliners only) |
| **Scores** | Lineup 2 · Stages 1 · Times 1 · Source 4 (facts) / 2 (full grid) · Clash **1** |
| **Genre** | House **HIGH** · Techno LOW · DnB LOW |
| **Size / maintain** | Mega · **MEDIUM** (few days) but **times may never be official** |
| **Rec** | **TIER A South Asia for Fit**; Clash **no** unless they publish a grid |

### Rank 27 — Echoes of Earth 2026 (India)

| | |
|---|---|
| **Basic** | Bengaluru (grounds historically HAL/ agrifarm — confirm 2026 venue on site). **5–6 Dec 2026**. [echoesofearth.com](https://echoesofearth.com/) |
| **Timezone** | `Asia/Kolkata` |
| **Data** | Official dates. Artist names on homepage (**careful: page mixes archive years**). Multiple conceptual stages historically. House/downtempo/live electronic more than peak-time techno/DnB. **No timetable found.** |
| **Status** | `LINEUP_TBA` (verify 2026-only names before ingest) |
| **Scores** | Lineup 3 · Stages 2 · Times 1 · Source 4 · Clash 2 |
| **Genre** | House **MEDIUM–HIGH** · Techno MEDIUM · DnB LOW |
| **Size / maintain** | Medium · **MEDIUM** |
| **Rec** | **TIER A South Asia** after you filter the artist list to 2026 |

### Rank 28 — Lollapalooza India 2027

| | |
|---|---|
| **Basic** | Mahalaxmi Racecourse, Mumbai. **23–24 Jan 2027**. Multi-genre. Press lineup includes **Steve Angello, 2manydjs** plus rock/pop. Tickets BookMyShow / lollaindia.com |
| **Timezone** | `Asia/Kolkata` |
| **Data** | Dates confirmed. Lineup in Hindustan Times etc. **Four stages.** Times unlikely until week-of, if ever. Weak electronic identity. |
| **Status** | `LINEUP_TBA` |
| **Scores** | Lineup 4 · Stages 3 · Times 1 · Source 3 (press vs official PDF) · Clash 2 |
| **Genre** | House MEDIUM · Techno LOW · DnB LOW |
| **Size / maintain** | Mega · **HARD** |
| **Rec** | **TIER B** India Fit only; **not** Clash |

### Rank 28a — 808 Festival Bangkok 2026 (Thailand) — **in window, nearest SEA Clash candidate**

| | |
|---|---|
| **Basic** | BITEC Bangna, Bangkok. **2–3 Oct 2026**. Two-stage EDM + techno/house. Official: [808festival.net](https://808festival.net/) |
| **Timezone** | `Asia/Bangkok` |
| **Data** | Dates + venue on organizer/press. **Two stages** (organizer). Phase 2 roster includes **Adam Beyer B2B Chris Avantgarde, 999999999, Argy, Giorgia Angiuli (live)** plus EDM (Illenium, W&W, Slander B2B Nghtmre). **No official set times found.** Tickets via Ticketmelon. |
| **Status** | `LINEUP_TBA` |
| **Scores** | Lineup 4 · Stages 4 (count known) · Times 1 / 4 expected · Source 4 · Updates 4 · **Clash 4** (if a 2-stage grid drops) |
| **Genre** | Techno **HIGH** (subset) · House MEDIUM · DnB LOW · rest is bass/big-room |
| **Size / maintain** | Medium indoor/expo, **two days** · **EASY–MEDIUM** |
| **Rec** | **TIER A SEA** — first SEA Clash attempt **only** if official timetable exists |

### Rank 28b — EDC Thailand 2026 (Phuket)

| | |
|---|---|
| **Basic** | Rhythm Park, Laguna Phuket. **18–20 Dec 2026**. Insomniac + Future Vibes. Official: [thailand.edc.com](https://thailand.edc.com/en/lineup/) · stages: [thailand.edc.com/en/experience/stages/](https://thailand.edc.com/en/experience/stages/) · hours: [thailand.edc.com/en/guide/hours-info/](https://thailand.edc.com/en/guide/hours-info/) |
| **Timezone** | `Asia/Bangkok` |
| **Data** | **Best SEA official catalog today.** Hours **15:00–24:00 daily**. **Six named stages:** kineticFIELD, circuitGROUNDS (Bassrush / D&B + bass), neonGARDEN (Factory 93 house/techno), stereoBLOOM, bionicJUNGLE (house), BOOMBOX Art Car (regional). Official artist list includes **Andy C, Kanine B2B Sota, Charlotte de Witte, Jamie Jones, CamelPhat, I Hate Models, VTSS, Loco Dice, Hannah Laing** plus trance/hardstyle/EDM. FAQ: set times posted **days leading up**; lineup subject to change. 20+. |
| **Status** | `LINEUP_TBA` (artists + stages; not clash grid) |
| **Scores** | Lineup 5 · Stages 5 · Times 2 (door) / 4 expected · Source 5 · Updates 4 · Clash 3 |
| **Genre** | DnB **HIGH** (circuitGROUNDS) · House **HIGH** · Techno **HIGH** (neonGARDEN) |
| **Size / maintain** | Large, 3 days, 6 stages · **HARD** |
| **Rec** | **TIER A SEA Fit**; Clash **only** after official set times |

### Rank 28c — Djakarta Warehouse Project 2026 (Indonesia)

| | |
|---|---|
| **Basic** | JIEXPO Kemayoran, Jakarta. **11–13 Dec 2026**. Official: [dwpfest.com](https://dwpfest.com/) |
| **Timezone** | `Asia/Jakarta` |
| **Data** | Dates on official ticket site + Ismaya/RRI. Phase one: **Afrojack, Alok, Axwell B2B Sebastian Ingrosso, Dimitri Vegas, R3HAB, Dimension, Eli & Fur, Lilly Palmer, Maddix, Cosmic Gate, Billy Gillies, MaRLo, Tye Turner**, SYBER host. **Garuda Land** named as mainstage; other stages not fully listed on the homepage scrape. Same weekend as Tomorrowland Thailand (different country). |
| **Status** | `LINEUP_TBA` (wave 1) |
| **Scores** | Lineup 3 · Stages 2 · Times 1 · Source 4 · Updates 3 · Clash 2 |
| **Genre** | House **HIGH** · Techno MEDIUM · DnB MEDIUM (Dimension) |
| **Size / maintain** | Large · **MEDIUM–HARD** |
| **Rec** | **TIER A SEA Fit** |

### Rank 28d — Tomorrowland Thailand 2026

| | |
|---|---|
| **Basic** | Wisdom Valley, Khao Mai Kaew / Bang Lamung, Chonburi (near Pattaya). **11–13 Dec 2026**. Official: [thailand.tomorrowland.com](https://thailand.tomorrowland.com/en/welcome/) · hours: [opening-hours](https://thailand.tomorrowland.com/en/practical/opening-hours/) · press lineup: [press.thailand.tomorrowland.com](https://press.thailand.tomorrowland.com/tomorrowland-thailand-unveils-its-consciencia-line-up-ahead-of-a-historic-debut-at-wisdom-valley) |
| **Timezone** | `Asia/Bangkok` |
| **Data** | Sold out. Official hours **Fri/Sat 13:00–01:00, Sun 13:00–00:00**. Press: **100+ artists, 6–7 stages** (Mainstage, CORE, FREEDOM + Thailand-only stages). Names include **Swedish House Mafia, Martin Garrix, Netsky, Andromedik, Lilly Palmer, Kölsch, Kevin de Vries, Nakadia**, plus big-room. Public lineup page is JS-heavy (“more artists to be announced”); **use the press list + official day filters, not blogs**. **No set times.** |
| **Status** | `LINEUP_TBA` |
| **Scores** | Lineup 5 · Stages 4 (named format) · Times 2 (gates) · Source 5 · Updates 3 · **Clash 1** |
| **Genre** | House **HIGH** · Techno MEDIUM · DnB MEDIUM (subset) |
| **Size / maintain** | Mega (50k+/day claimed) · **HARD** |
| **Rec** | **TIER A SEA Fit / REJECT Clash** — same product rule as Tomorrowland Belgium |

### Rank 28e — Wonderfruit 2026 (Thailand)

| | |
|---|---|
| **Basic** | The Fields, Siam Country Club, Chonburi. **3–7 Dec 2026**. Official: [wonderfruit.co](https://wonderfruit.co/) · FAQ: [wonderfruit.co/faq](https://wonderfruit.co/faq) |
| **Timezone** | `Asia/Bangkok` |
| **Data** | Dates, camping, **gate windows** official (Thu 16:00–midnight, Fri–Sun 08:00–midnight). **24h programming** some venues. Full schedule **later on Wonder App + web profiles**. Directory of venues/programs exists but is not a clash grid. Arts/wellness/food + electronic; **not** a pure D&B/house/techno fest. Related: Chapters Kyoto Oct 2026 (Japan — out of SEA), Din Daen 29–31 Jan 2027 (separate event). |
| **Status** | `IDENTITY_ONLY` / rolling program |
| **Scores** | Lineup 2 · Stages 3 (venues) · Times 2 (gates) · Source 5 · Updates 3 · Clash 2 |
| **Genre** | House MEDIUM · Techno MEDIUM · DnB LOW |
| **Size / maintain** | Large, 5 days, many venues · **HARD** |
| **Rec** | **TIER B SEA** — Fit if you want a Thai boutique identity; Clash no |

### Rank 28f — Dekmantel at Potato Head Bali 2026

| | |
|---|---|
| **Basic** | Desa Potato Head, Seminyak. **Sat 26 Sep 2026**, ~14:00–04:00 across Beach Club + Klymax. Official: [seminyak.potatohead.co/.../dekmantel-2026](https://seminyak.potatohead.co/experience/potato-head-presents-dekmantel-2026) |
| **Timezone** | `Asia/Makassar` (Bali / WITA) |
| **Data** | Full named lineup: **Avalon Emerson, Dekmantel Soundsystem, Ogazón, Soichi Terada LIVE, Fafi Abdel Nour**, plus Indonesian artists. **Two rooms**, one day. Per-set times may stay booth-only. |
| **Status** | `LINEUP_TBA` (artists yes; times maybe never public) |
| **Scores** | Lineup 5 · Stages 4 · Times 2 · Source 5 · Clash 3 |
| **Genre** | House **HIGH** · Techno **HIGH** · DnB LOW |
| **Size / maintain** | Boutique · **EASY** |
| **Rec** | **TIER A SEA** if the date is still ahead of ingest; skip if already over |

### Rank 28g — Gaia Beats 2027 (Chiang Mai)

| | |
|---|---|
| **Basic** | Sense Hot Spring, Mae On, Chiang Mai. **22–24 Jan 2027**. Official: [gaiabeats.com](https://gaiabeats.com/) |
| **Timezone** | `Asia/Bangkok` |
| **Data** | Dates + tickets official. Site claims house / techno / D&B / psy and “first round lineup” but public page showed **0 artists / 0 stages** (likely JS). Boutique camping, ≥50% Thai artists promised. |
| **Status** | `IDENTITY_ONLY` |
| **Scores** | Lineup 1 · Stages 1 · Times 1 · Source 4 · Clash 3 (size) / 1 (data today) |
| **Genre** | House MEDIUM · Techno MEDIUM · DnB MEDIUM (claimed) |
| **Size / maintain** | Small · **EASY** once lineup is HTML |
| **Rec** | **TIER C wishlist** until names are on the official site |

### Rank 28h — The Labyrinth 2026 (Japan) — **best JP boutique in-window**

| | |
|---|---|
| **Basic** | Minakami Hodaigi Camp Site, Gunma. **10–12 Oct 2026**. Official: [mindgames.jp/laby26](https://www.mindgames.jp/laby26) · info: [mindgames.jp/info](https://www.mindgames.jp/info) |
| **Timezone** | `Asia/Tokyo` |
| **Data** | Dates, camping, English FAQ. **Music hours official (tentative):** Sat 18:00–02:00, Sun 10:00–01:30, Mon 10:00–18:00. **“Line-up will be released soon; complete timetable the week of the event.”** Historically **one Funktion-One floor** (sometimes a small second area — verify). Tickets Zaiko; Categories 1–3 sold out. |
| **Status** | `IDENTITY_ONLY` |
| **Scores** | Lineup 1 · Stages 2 (likely 1) · Times 2 (blocks) / 4 expected week-of · Source **5** · Updates 4 · **Clash 1–2** |
| **Genre** | House **HIGH** · Techno **HIGH** · DnB LOW |
| **Size / maintain** | Boutique camping · **EASY** (few stages) |
| **Rec** | **TIER A JP Fit/identity**. Not a Clash demo unless a second named floor appears on the timetable |

### Rank 28i — Ultra Japan 2026

| | |
|---|---|
| **Basic** | Tokyo Odaiba Ultra Park. **19–20 Sep 2026**. Official: [ultrajapan.com/lineup](https://ultrajapan.com/lineup) · running order: [ultrajapan.com/lineup/stage-running-order](https://ultrajapan.com/lineup/stage-running-order) |
| **Timezone** | `Asia/Tokyo` |
| **Data** | **This weekend** relative to research date. Gates 10:00 / show 11:00 / end ~21:00. **Three named stages:** MAIN, RESISTANCE, ULTRA PARK. Official Avex PR lists artists by day/stage (Peggy Gou, Martinez Brothers, Sara Landry, Lilly Palmer, Ken Ishii B2B Risa Taniguchi on Resistance; mainstage EDM). **Official running-order is a PNG**, not HTML — transcribe by hand if you still need a Clash demo this week. 2027 **not announced**. |
| **Status** | `LINEUP_TBA` (artists + stages) → possible `SCHEDULE_VERIFIED` if PNG times are readable |
| **Scores** | Lineup 5 · Stages 5 · Times 3 (image) · Source 5 · Clash 3 |
| **Genre** | House MEDIUM (Resistance) · Techno MEDIUM · DnB LOW · rest big-room |
| **Size / maintain** | Large urban, 2 days · **MEDIUM** |
| **Rec** | **TIER B JP** — ingest only if the weekend is still ahead; otherwise skip (historical) |

### Rank 28j — Snow Machine Japan 2027 (Hakuba)

| | |
|---|---|
| **Basic** | Hakuba Valley, Nagano. **2–7 Mar 2027**. Festival nights **Thu 4 – Sat 6 Mar** (2 stages). Official: [hakuba.snow-machine.com](https://hakuba.snow-machine.com/) · NZ hub also lists Main Arena: [snow-machine.nz/festival](https://www.snow-machine.nz/festival/) |
| **Timezone** | `Asia/Tokyo` |
| **Data** | Official artist list includes **Wilkinson, Kanine, Luude** (DnB/bass), **Kettama, Patrick Topping, Torren Foot** (house), **Rebūke, Risa Taniguchi** (techno), plus Elderbrook / TOKiMONSTA. **Après stages at multiple ski resorts** + Main Arena = multi-venue. Packages bundle lift passes. No per-set times. |
| **Status** | `LINEUP_TBA` |
| **Scores** | Lineup 4 · Stages 2 (2 + après) · Times 1 · Source 4 · Clash **1** |
| **Genre** | DnB **HIGH** (subset) · House **HIGH** · Techno MEDIUM |
| **Size / maintain** | Medium crowd, **HARD** logistics |
| **Rec** | **TIER A JP Fit**; Clash **no** |

### Rank 28k — GMO SONIC 2027 (Saitama)

| | |
|---|---|
| **Basic** | GMO Arena Saitama (ex Super Arena). **3–4 Apr 2027**. Official: [sonic.gmo/en](https://sonic.gmo/en/) |
| **Timezone** | `Asia/Tokyo` |
| **Data** | Dates + venue official. **Artists TBA.** Historically indoor EDM mega (Creativeman). Arena volume = Creamfields-adjacent. |
| **Status** | `IDENTITY_ONLY` |
| **Scores** | Lineup 1 · Stages 2 · Times 1 · Source 5 · Clash 2 |
| **Genre** | House MEDIUM · Techno LOW · DnB LOW (until poster) |
| **Size / maintain** | Mega indoor · **HARD** |
| **Rec** | **TIER B JP shell** |

### Rank 29 — Draaimolen (Netherlands) — 2027 TBD

| | |
|---|---|
| **Basic** | Tilburg area forest; 2026 was **4–5 Sep 2026** (just before this research). Techno/experimental. 2027 **not announced** in official results. |
| **Status** | Skip 2027 until dates exist |
| **Rec** | **TIER C wishlist** — historically **excellent** official program; add the week dates drop |

### Rank 30–36 — Fill-ins (include if space)

| Festival | Country | Dates (window) | Why include | Status | Rec |
|---|---|---|---|---|---|
| **Junction 2 2027** | UK | TBA (2026 already ran) | Best UK techno site (Boston Manor, 2–3 stages) | Wait for dates | TIER C |
| **Mysteryland 2027** | NL | 2027 return after 2026 hiatus; dates TBD | House/EDM; large | IDENTITY when dated | TIER C |
| **Dimensions / Defected Croatia** | HR | 2026 editions **over** (late Jul/Aug 2026) | Tisno house/techno gold historically | Wait 2027 pages | TIER C 2027 |
| **Magnetic Fields / Nomads** | IN | Nomads **13–15 Feb 2026 already over** | Best Indian boutique electronic historically; 8 named stages on 2026 site | 2027 unknown | TIER C |
| **NH7 Weekender** | IN | Mar 2026 over; 2027 unknown | Multi-genre, little techno/DnB | Poor genre | REJECT / C |
| **FirstLight Karachi** | PK | **19 Sep 2026** (days after research) | One-day house (Axwell) | Concert not festival | REJECT for catalog |
| **Wonderland / YAGA Colombo** | LK | Nov 2026 / scattered 2026 | One-nighters, surprise lineup | IDENTITY event | REJECT as “festival” |
| **Day Zero Bali 2027** | ID | TBD (“Returns 2027”) | House/techno boutique historically | IDENTITY | TIER C |
| **S2O Songkran 2027** | TH | TBD (expect April) | Recurring EDM water fest | Wait official dates | TIER C |
| **Rainbow Disco Club 2027** | JP | TBD (Golden Week historically) | Best JP open-air house/techno historically | 2026 **over** (17–19 Apr) | TIER C |
| **rural 2027** | JP | TBD | Best JP camping techno historically | 2026 **over** (17–20 Jul) | TIER C |

---

## 4. Master table (recommended working set)

| Rank | Festival | Country | Dates | Genre | 2026/27 status | Lineup? | Stages? | Exact times? | Data /5 | Clash /5 | Maintain | Official source | Rec |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| 1 | Let It Roll Winter | CZ | 22–23 Jan 2027 | DnB | Confirmed | Partial | Yes (3) | Not yet (promised ~14d) | 4 | 5 | EASY | letitroll.eu | **A** |
| 2 | Time Warp DE | DE | 3 Apr 2027 | Techno | Confirmed | No | Format 5 floors | Not yet | 3 | 5 | EASY | time-warp.de | **A** |
| 3 | Soenda | NL | 29 May 2027 | Techno/house | Confirmed + hours | No | Hist. yes | Door 12–23 | 3 | 5 | EASY | soenda.net | **A** |
| 4 | DGTL Amsterdam | NL | 26–28 Mar 2027 | House/techno | Confirmed + session hours | No | Hist. yes | Blocks only | 3 | 4 | MED | dgtl-festival.com | **A** |
| 5 | Liquicity | NL | 23–25 Jul 2027 | DnB | Dates | No | Hist. 3 | Later | 3 | 5 | EASY | festival.liquicity.com | **A** |
| 6 | Kappa Futur | IT | 2–4 Jul 2027 | House/techno | Dates+tix | 2026 hist. on site | Hist. | Later HTML | 3 | 4 | MED | kappafuturfestival.it | **A** |
| 7 | Rampage Weekend | BE | 5–7 Mar 2027 | DnB | Confirmed | No | Indoor areas | Later | 3 | 4 | MED | rampage.eu | **A** |
| 8 | Hospitality Beach | HR | 30 Jun–5 Jul 2027 | DnB | Tix live | No | 5 stated | Later | 3 | 5 | MED | hospitalityonthebeach.com | **A** |
| 9 | Awakenings Festival | NL | 9–11 Jul 2027 | Techno | Confirmed | No | Hist. | Later | 3 | 4 | MED | awakenings.com | **A** |
| 10 | Awakenings UpClose | NL | 15–16 May 2027 | Techno | Confirmed | No | Intimate | Later | 3 | 5 | EASY | awakenings.com | **A** |
| 11 | Extrema Outdoor | BE | 14–16 May 2027 | House/techno | Confirmed | No | Hist. | Later | 3 | 4 | MED | extrema.be | **A** |
| 12 | Let It Roll Summer | CZ | 5–8 Aug 2027 | DnB | Signup | No | 6+ | Later | 3 | 3 | HARD | letitroll.eu | **A** TBA |
| 13 | Verknipt Festival | NL | 5–6 Jun 2027 | Hard techno | Date+hours | No | Hist. | Later | 3 | 4 | EASY | verknipt.org | **A** |
| 14 | Loveland | NL | 7–8 Aug 2027* | House | FAQ | No | Hist. | Hours | 3 | 4 | MED | loveland.nl | **A** |
| 15 | Awakenings ADE | NL | 21–25 Oct 2026 | Techno | **Now** | Yes (events) | 1 venue / 8 nights | Event windows | 4 | 3 | MED | awakenings.com | **A** |
| 16 | DGTL ADE | NL | 22–25 Oct 2026 | House/techno | **Now** | Partial | 2 venues | Later | 3 | 3 | MED | dgtl-festival.com | **B** |
| 17 | Hospitality Weekender | UK | 12–15 Mar 2027 | DnB | Listed | Tiny | Hist. | Later | 2 | 4 | MED | lock official URL | **B** |
| 18 | Dekmantel | NL | 28 Jul–1 Aug 2027 | House/techno | Pre-reg | No | Multi-venue | App later | 3 | 3 | HARD | dekmantelfestival.com | **B** |
| 19 | Hideout | HR | 29 Jun–2 Jul 2027 | House | Packages | No | Beach multi | Later | 2 | 3 | HARD | hideoutfestival.com | **B** |
| 20 | Outlook | HR | ~22–26 Jul 2027* | DnB | Verify | No | Tisno | Later | 2 | 4 | MED | outlookfestival.com | **B** |
| 21 | Verknipt Croatia | HR | 11–15 Jul 2027 | Techno | Official | Check | Holiday | Later | 2 | 3 | HARD | verkniptcroatia.org | **B** |
| 22 | Sónar | ES | ~17–19 Jun 2027* | Electronic | Verify | No | Multi | Hist. excellent | 2 | 3 | HARD | sonar.es | **B** |
| 23 | Sunburn | IN | 18–19 Dec 2026 | House/EDM | Confirmed | Headliners | No | No | 3 | 1 | MED | sunburn.in | **A SA Fit** |
| 24 | Echoes of Earth | IN | 5–6 Dec 2026 | House/elec | Confirmed | On site† | Hist. | No | 3 | 2 | MED | echoesofearth.com | **A SA** |
| 25 | Lollapalooza India | IN | 23–24 Jan 2027 | Multi | Confirmed | Press yes | 4 | No | 3 | 2 | HARD | lollaindia.com | **B SA** |
| 25a | **808 Bangkok** | TH | 2–3 Oct 2026 | Techno/EDM | Confirmed | Phase 2 | 2 | No | 4 | 4 | EASY–MED | 808festival.net | **A SEA** |
| 25b | **EDC Thailand** | TH | 18–20 Dec 2026 | DnB/house/techno/EDM | Confirmed | Official list | 6 named | Door 15–24 | 5 | 3 | HARD | thailand.edc.com | **A SEA Fit** |
| 25c | **DWP** | ID | 11–13 Dec 2026 | House/EDM + techno/DnB | Confirmed | Wave 1 | Mainstage named | No | 3 | 2 | MED–HARD | dwpfest.com | **A SEA Fit** |
| 25d | **Tomorrowland TH** | TH | 11–13 Dec 2026 | Dance mega | Sold out | Press 100+ | 6–7 | Gates yes | 4 | 1 | HARD | thailand.tomorrowland.com | **A Fit / Clash no** |
| 25e | **Wonderfruit** | TH | 3–7 Dec 2026 | Arts + elec | Confirmed | Rolling | Many venues | Gates; app later | 3 | 2 | HARD | wonderfruit.co | **B SEA** |
| 25f | **Dekmantel Bali** | ID | 26 Sep 2026 | House/techno | Confirmed | Yes | 2 rooms | Hours ~14–04 | 4 | 3 | EASY | potatohead.co | **A SEA** |
| 25g | **Gaia Beats** | TH | 22–24 Jan 2027 | Mixed elec | Dates+tix | Not public | Unknown | No | 2 | 3 | EASY | gaiabeats.com | **C** |
| 25h | **Labyrinth** | JP | 10–12 Oct 2026 | House/techno | Confirmed + hours | Soon | Hist. 1 floor | Week-of promised | 3 | 1 | EASY | mindgames.jp | **A JP Fit** |
| 25i | **Ultra Japan** | JP | 19–20 Sep 2026 | EDM + Resistance | **This weekend** | Yes | 3 named | Official PNG | 4 | 3 | MED | ultrajapan.com | **B JP** |
| 25j | **Snow Machine** | JP | 2–7 Mar 2027 | House/DnB/ski | Confirmed | Yes | 2 + après | No | 3 | 1 | HARD | hakuba.snow-machine.com | **A JP Fit** |
| 25k | **GMO SONIC** | JP | 3–4 Apr 2027 | EDM arena | Dates | No | Arena | No | 2 | 2 | HARD | sonic.gmo | **B JP** |
| 26 | Unsound | PL | 2–11 Oct 2026 | Experimental | **Schedule live** | Yes | Venues | Starts yes | 4 | 2 | HARD | unsound.pl | **C** |
| 27 | Creamfields | UK | ~Aug 2027 | Dance mega | Fuzzy | Prodigy | Many | No | 2 | 1 | HARD | creamfields.com | **C** |
| 28 | Boom | PT | 18–25 Jul 2027 | Psy/trance | Confirmed | No | Many | Late | 2 | 2 | HARD | boomfestival.org | **C** |
| 29 | Draaimolen 2027 | NL | TBD | Techno | Wait | — | — | Hist. great | — | 4 | EASY | draaimolen.com | **C** |
| 30 | Junction 2 2027 | UK | TBD | Techno | Wait | — | 2–3 | Hist. good | — | 5 | EASY | junction2.london | **C** |
| 31 | Mysteryland 2027 | NL | TBD | House | Hiatus return | — | Many | — | — | 2 | HARD | mysteryland.com | **C** |
| 32 | Magnetic Fields 2027 | IN | TBD | Boutique elec | 2026 Nomads over | Hist. 8 stages | Hist. | Hist. | — | 4 | MED | magneticfields.in | **C** |
| 33 | Defected/Dimensions 2027 | HR | TBD | House/techno | 2026 over | — | 3 Tisno | Hist. | — | 5 | MED | defected.com / dimensionsfestival.com | **C** |

\*Confirm on official site before ingest.  
†Echoes homepage mixes years — ingest only 2026-labelled names.

---

## 5. Cut lists you asked for

### Top 10 for Clash Engine (when official times drop)

1. Let It Roll Winter 2027  
2. Soenda 2027  
3. Time Warp Mannheim 2027  
4. Awakenings UpClose 2027  
5. Liquicity 2027  
6. Hospitality on the Beach 2027  
7. Verknipt Utrecht 2027  
8. DGTL Amsterdam 2027  
9. Rampage Weekend 2027  
10. Extrema Outdoor 2027 **or** Kappa Futur (if you want house headliners)

**Do not** put ADE-the-city, Creamfields, Boom, Hideout boats, or Dekmantel city nights in this ten.

### Top 10 for Festival Fit (lineup names matter; times optional)

1. **EDC Thailand 2026** (official named stages + large D&B/house/techno roster **now**)  
2. Awakenings ADE 2026 (names exist **now**)  
3. **Tomorrowland Thailand 2026** (100+ official names; **Fit only**)  
4. **DWP 2026** (phase one includes Dimension, Lilly Palmer, Eli & Fur)  
5. **808 Festival Bangkok 2026** (Adam Beyer b2b, 999999999, Argy — 2 stages)  
6. Sunburn 2026 (headliners now)  
7. Echoes of Earth 2026  
8. Kappa Futur 2027 (huge names once 2027 poster drops)  
9. Let It Roll Summer 2027 / Hospitality Beach (DnB density)  
10. Dekmantel 2027 *or* **Dekmantel Bali 2026** (tasteful house/techno; Bali is one day)

Lollapalooza India 2027 drops off this ten; keep it as India Fit-only filler, not a top Fit row.

### Top South Asian candidates (honest)

| Priority | Name | Why | Don’t expect |
|---|---|---|---|
| 1 | **Sunburn 2026** | Official dates/venue; headliners; Asia’s only large electronic fest in-window | Set times, DnB, techno identity |
| 2 | **Echoes of Earth 2026** | Official site, Dec dates, artist culture, boutique | Clash grid; filter archive names |
| 3 | **Lollapalooza India 2027** | Confirmed + published lineup in press | Electronic-first catalog; times |
| 4 | **Magnetic Fields 2027** | Best *historical* Indian electronic data (named stages) | Anything until 2027 edition exists |
| 5 | **Sunburn Arena Bengaluru 18 Dec 2026** | Same week as festival (DJ Snake) — **concert**, optional | Calling it a festival |
| — | Pakistan / Bangladesh / Nepal **festivals** | No maintainable multi-day official electronic catalog found | Padding the list |
| — | Sri Lanka YAGA / Wonderland | One night, surprise lineup | V1 festival entity |

**Do not force 10 SA festivals.**

### Top Southeast Asian candidates (honest)

| Priority | Name | Why | Don’t expect |
|---|---|---|---|
| 1 | **808 Festival Bangkok 2026** | Official site, **2 stages**, Oct dates, techno names on the bill | Full clash grid today; DnB identity |
| 2 | **EDC Thailand 2026** | Best SEA **official** stages + artist HTML; D&B/house/techno tents exist | Set times until week-of; easy maintain |
| 3 | **DWP 2026** | Official ticket site + phase-one names (Dimension, Lilly Palmer, Eli & Fur) | Complete stage map / times |
| 4 | **Dekmantel Bali 2026** | Genre-pure house/techno, 2 rooms, official Potato Head page | Public per-set times |
| 5 | **Tomorrowland Thailand 2026** | Official hours + huge press lineup | Clash, or treating the JS lineup page as complete |
| 6 | **Wonderfruit 2026** | Official dates/gates; unique Thai boutique | Pure dance identity; HTML timetable (app-first) |
| 7 | **Gaia Beats 2027** | Official Jan dates, claimed D&B/house/techno | Any artists until they render on gaiabeats.com |
| — | **S2O 2027** | Recurring Songkran EDM water fest | Anything until 2027 dates (2026 was 11–13 Apr — **already over**) |
| — | **Day Zero Bali 2027** | Official site: “Returns 2027” | Dates, venue, lineup |
| — | **ZoukOut / Ultra SG / Ultra PH** | Historically important | 2026/27 editions — **not found** |
| — | **We The Fest** | Jakarta brand | Electronic-first catalog |
| — | Full Moon / Koh Phangan / Circoloco club weeks | Parties | Festival entity in our model |

**Do not force 10 SEA festivals.** Thailand + Indonesia already beat India on official 2026 pages. Singapore / Malaysia / Philippines / Vietnam / Cambodia did not yield a second maintainable multi-day electronic catalog in this window.

### Top Japanese candidates (honest)

| Priority | Name | Why | Don’t expect |
|---|---|---|---|
| 1 | **The Labyrinth 2026** | Official English site, music hours, week-of timetable promise, house/techno pilgrimage | Named lineup today; a Clash graph (often **one floor**) |
| 2 | **Snow Machine Hakuba 2027** | Official D&B + house names (Wilkinson, Kanine, Kettama, Patrick Topping) | Simple one-site Clash; times |
| 3 | **Ultra Japan 2026** | 3 stages + official running-order PNG **this weekend** | 2027 dates; DnB; HTML timetable |
| 4 | **GMO SONIC 2027** | Official Apr dates at Saitama arena | Lineup, techno identity |
| — | **Rainbow Disco Club 2027** | Historically the JP house/techno outdoor gold standard | Anything until 2027 dates are on rainbowdiscoclub.com |
| — | **rural 2027** | Historically the JP camping techno gold standard | Anything until ruraljp.com dates 2027 |
| — | Fuji Rock / Summer Sonic | Famous | Electronic-first catalog |
| — | Rampage Tokyo / WOMB 06S | Real DnB culture | Festival entity (club nights; Rampage Vol.2 was **30 May 2026 — over**) |

**Do not force 10 JP festivals.** Japan’s *scene* is excellent. Japan’s *dated 2026/27 festival catalog* is small.

---

## 6. Do not include (famous but bad V1 data)

| Festival | Why reject / defer |
|---|---|
| **Tomorrowland Belgium** | Mega, two weekends + Harmony, insane stage count, commercial IP, clash unmaintainable |
| **Tomorrowland Thailand (Clash)** | First Asia edition is *smaller* than Boom, but still **6–7 stages / 100+ artists**. Official hours exist; **set times not published**. Use as **Fit-only**, same rule as Creamfields |
| **Fuji Rock / Summer Sonic / Rising Sun / Asagiri Jam** | Japanese icons; **not** D&B/house/techno-first. Asagiri skipping 2026 |
| **Glastonbury** | Not electronic-first; pyramid vs 100 clashes; BBC/official grid is huge |
| **Sziget / Primavera / Rock Werchter** | Multi-genre; electronic is a corner |
| **ADE as one festival** | 300 venues, 1200 events — not a Performance table Om can own |
| **Creamfields** (Clash) | Volume; times late; mixed EDM |
| **Mysteryland 2026 skip year** | 2027 dates not firm |
| **I Love Techno Ghent** | Brand left Ghent years ago |
| **Magnetic Fields Nomads Feb 2026** | **Already happened** |
| **NH7 Weekender 2026** | Already happened; indie/hip-hop not techno/DnB |
| **Festalgia / Frontstage “lineups”** | Some 2027 pages are **AI predictions** (e.g. Awakenings 2027 on Festalgia) — never ingest |
| **BookMyShow as source of truth** | Ticketing aggregator; scrape ecosystem; no timetable API |
| **RA unofficial APIs** | Scrapers |

---

## 7. Recommended final V1 catalog

**~24 Europe TIER A/B + ~5–7 Southeast Asia + ~3–4 Japan + ~3 India TIER A/B + wishlist C**

### Europe — add now as shells (`IDENTITY_ONLY` / `LINEUP_TBA`)

Let It Roll Winter, Time Warp DE, Soenda, DGTL Amsterdam, Liquicity, Kappa Futur, Rampage Weekend, Hospitality on the Beach, Awakenings Festival, Awakenings UpClose, Extrema Outdoor, Let It Roll Summer, Verknipt Utrecht, Loveland, Awakenings ADE 2026, DGTL ADE 2026, Hospitality Weekender (after official URL), Dekmantel (Fit-only flag).

**Optional B:** Hideout, Outlook, Verknipt Croatia, Sónar.

**Not in V1 live Clash:** Creamfields, Boom, Unsound (unless pipeline test), ADE-city.

### South Asia — add now

1. Sunburn 2026 (`LINEUP_TBA`)  
2. Echoes of Earth 2026 (`LINEUP_TBA` after name hygiene)  
3. Lollapalooza India 2027 (`LINEUP_TBA`, Fit-only)

**Wishlist only:** Magnetic Fields 2027, Defected/Dimensions 2027, Draaimolen 2027, Junction 2 2027.

### Southeast Asia — add now

1. **808 Festival Bangkok 2026** (`LINEUP_TBA`, nearest SEA Clash candidate — 2 stages)  
2. **EDC Thailand 2026** (`LINEUP_TBA`, best SEA Fit; Clash only if Insomniac posts set times)  
3. **DWP 2026** (`LINEUP_TBA`, Jakarta; phase one only)  
4. **Dekmantel Bali 2026** (`LINEUP_TBA`, 26 Sep — house/techno boutique)  
5. **Tomorrowland Thailand 2026** (`LINEUP_TBA`, **Fit-only** — do not Clash)  
6. Optional: **Wonderfruit 2026** (`IDENTITY_ONLY` until Wonder App timetable; mixed arts/electronic)

**Wishlist:** Gaia Beats Chiang Mai Jan 2027 (dates official, lineup not readable yet), Day Zero Bali 2027 (site says “Returns 2027” only), S2O Songkran 2027 (wait for dates).

### Japan — add now

1. **The Labyrinth 2026** (`IDENTITY_ONLY` → lineup when Mindgames posts; **Clash unlikely** — typically one floor)  
2. **Snow Machine Hakuba 2027** (`LINEUP_TBA`, Fit; Clash no — ski villages + après stages)  
3. **GMO SONIC 2027** (`IDENTITY_ONLY`, Fit later)  
4. Optional near-term: **Ultra Japan 2026** (`LINEUP_TBA` / possible running-order transcription) — **19–20 Sep 2026 only**. Skip if ingest is after the weekend.

**Wishlist:** Rainbow Disco Club 2027, rural 2027 (both historically **better** than Ultra for our genres; wait for official dates). Ultra Japan 2027.

**Total live V1: ~21–25 EU + 5–7 SEA + 3–4 JP + 3 IN.** Do not swap Europe Clash demos for JP fame.

---

## 8. Which ~10 get COMPLETE `SCHEDULED` data first (Clash demo)

Transcribe **only when the official timetable exists**. Order by **date** so the demo is real:

| Order | Festival | Why first | Likely timetable |
|---|---|---|---|
| 1 | **Awakenings ADE 2026** | Soonest (Oct); named nights | Event hours now; set times maybe week-of |
| 2 | **DGTL ADE 2026** | Same week, 2 venues | Official event pages |
| 3 | **Let It Roll Winter 2027** | **Best Clash shape** | Official ~14 days before (early Jan 2027) |
| 4 | **Rampage Weekend 2027** | Indoor, Mar | Typical late grid |
| 5 | **Time Warp DE 2027** | 5 floors, one night | Usually close to April |
| 6 | **Awakenings UpClose 2027** | Small May techno | Official site/app |
| 7 | **Extrema Outdoor 2027** | May, official site | HTML/PDF historically |
| 8 | **Soenda 2027** | One Saturday | Simple graph |
| 9 | **Verknipt Utrecht 2027** | Two days, hours known | Official |
| 10 | **Liquicity 2027** *or* **Hospitality Beach** | Pure DnB, 3–5 stages | Summer; don’t start both complete |

**Sunburn / Echoes / Lolla India / Tomorrowland Thailand / Wonderfruit:** do **not** force fake end times. Stay `TBA`.

**808 Bangkok:** if they publish a 2-stage PDF/HTML before 2 Oct 2026, it can jump the Clash queue ahead of ADE (sooner, simpler graph). Do not invent times from Housem.nl-style blogs.

---

## 9. Which stay `LINEUP_TBA` initially

- All 2027 outdoor fests **until** official timetable PDF/HTML  
- Sunburn 2026  
- Echoes of Earth 2026  
- Lollapalooza India 2027  
- Let It Roll Summer (too many sets for first pass)  
- Dekmantel, Hideout, Sónar, Creamfields  
- Boom 2027  
- Tomorrowland Thailand, EDC Thailand, DWP, Wonderfruit (until official set times)  
- Gaia Beats 2027, Day Zero Bali 2027, S2O 2027  
- Labyrinth 2026, Snow Machine 2027, GMO SONIC 2027, Ultra Japan 2026 (if still in window)

**Identity only (no fake artists):** Mysteryland 2027, Junction 2 2027, Magnetic Fields 2027, Draaimolen 2027, Day Zero Bali 2027, S2O 2027, Rainbow Disco Club 2027, rural 2027.

---

## 10. Backend fields to add (from this research)

You already have festival/artist/stage/performance. Add:

**Festival**

| Field | Why |
|---|---|
| `timezone` (IANA) | `Europe/Amsterdam` vs `Asia/Kolkata` — Clash is wrong without it |
| `coverage_tier` | `IDENTITY` / `LINEUP_TBA` / `SCHEDULE_VERIFIED` |
| `verification_status` | `UNVERIFIED` / `REVIEWED` / `PUBLISHED` / `TAKEN_DOWN` |
| `region` | `EUROPE` / `SOUTHEAST_ASIA` / `JAPAN` / `SOUTH_ASIA` |
| `edition_year` + `start_date`/`end_date` | Already have dates; keep edition explicit |
| `official_website` | You have this; make it required for V1 |
| `official_timetable_url` | Distinct from homepage |
| `timetable_format` | `HTML` / `PDF` / `ICS` / `NONE` |
| `expected_timetable_lead_days` | e.g. Let It Roll 14 |
| `source_url` / `lineup_as_of` | Provenance |
| `is_multi_venue` | Dekmantel, Unsound, ADE, Hideout |
| `clash_enabled` | Product gate: only true if `SCHEDULE_VERIFIED` |

**Performance**

| Field | Why |
|---|---|
| `verification_status` | Same as festival |
| `source_url` | Timetable row provenance |
| `time_precision` | `NONE` / `DOOR_WINDOW` / `START_ONLY` / `START_AND_END` |
| `ends_at` already exists | Never invent end = start+1h for Clash |

**Artist**

| Field | Why |
|---|---|
| `musicbrainz_id` | Fit joins later |
| `genre_tags` you own | Don’t import MusicBrainz NC tags |

**Do not** add scraper job IDs.

---

## 11. Operating notes for Om

1. **Today you cannot fill 30 clash-ready grids.** Anyone who says otherwise is using aggregators.  
2. **Create festival shells this month** for TIER A with dates + timezone + official URL.  
3. **Ingest artists** only from official posters (Awakenings ADE, **EDC Thailand lineup page**, DWP homepage wave, 808 phase 2, Sunburn headliners, Echoes 2026-only, Tomorrowland **press** list).  
4. **Calendar reminders:** 808 Bangkok ~late Sep 2026 timetable watch; Let It Roll Winter timetable ~8 Jan 2027; Awakenings/DGTL mid-October 2026.  
5. **Email** Let It Roll, Liquicity, Soenda, Hospitality, Time Warp for CSV/ICS — those orgs are small enough to answer. 808 Bangkok is small enough to try the same.  
6. **Never** copy Festalgia “projected” 2027 lineups.  
7. **SEA timezone:** Bangkok/Jakarta are `+07`; **Bali is `Asia/Makassar` (`+08`)** — do not reuse `Asia/Bangkok` for Potato Head.  
8. **Japan timezone:** everything here is `Asia/Tokyo`. Labyrinth week-of timetable watch: ~3–10 Oct 2026.

---

## 12. One-line answer

**Build V1 around the Dutch/Belgian/Czech/Tisno official-site circuit, then Thailand/Indonesia Fit events, then a small Japan set (Labyrinth Oct 2026, Snow Machine Mar 2027, optional Ultra this weekend), plus three Indian Fit-only events. Use October ADE week and January Let It Roll Winter as the first real Clash demos. 808 Bangkok is the only SEA Clash candidate. Labyrinth has a documented week-of timetable habit but may still be one floor. Do not chase Tomorrowland (Belgium or Thailand) for Clash, full ADE, or padded SA/SEA/JP lists.**

---

## 13. Southeast Asia research notes (13 Sep 2026)

**Countries checked:** Thailand, Indonesia, Singapore, Malaysia, Philippines, Vietnam, Cambodia (plus Laos/Myanmar/Brunei — no catalog-grade fests found).

**What is actually good**

- Thailand has a **real 2026 electronic festival season** (Oct 808 → Dec Wonderfruit / Tomorrowland / EDC), which India does not match for official stage+artist HTML.
- Indonesia has **DWP** (mainstage EDM with usable techno/DnB names) and a **Dekmantel one-dayer** that is closer to our Europe taste than Sunburn.
- Insomniac’s EDC Thailand site is the SEA equivalent of a Dutch organizer site: named stages, FAQ on set times, long official artist list.

**What looks big and is still bad data**

- Tomorrowland Thailand is a **Fit goldmine and a Clash trap** (same as Belgium, smaller site, still 6–7 stages).
- S2O aggregator “timetables” (e.g. Housem.nl for Apr 2026) are **not** `SCHEDULE_VERIFIED`.
- DWP 2026 is **not in Bali** this year (Jakarta JIEXPO). Music Festival Wizard still saying Bali is stale.
- Wonderfruit’s 24-hour claim is real per FAQ; that does **not** mean a typed clash grid exists today.

**Calendar collisions (product, not same site)**

- **11–13 Dec 2026:** Tomorrowland Thailand **and** DWP Jakarta.
- **3–7 Dec 2026:** Wonderfruit, then EDC Phuket **18–20 Dec**.
- **2–3 Oct 2026:** 808 Bangkok, overlapping Europe’s Unsound / ADE-adjacent autumn.

**Rejected / deferred SEA**

| Name | Reason |
|---|---|
| S2O 2026 | Already ran 11–13 Apr 2026 |
| S2O 2027 | No official dates |
| Day Zero Bali 2026 | Already ran 17 Apr 2026 (week 14–19) |
| Day Zero Bali 2027 | “Returns 2027” only |
| Airtime Asia Da Nang | Aug 2026 already over; 2027 unconfirmed |
| ZoukOut Singapore | Last confirmed edition found: Dec 2023 |
| Ultra Singapore / Philippines | 2015–16 history; no 2026/27 SEA Ultra catalog |
| We The Fest | Indie/multi-genre; not D&B/house/techno-first |
| Full Moon / Jungle Experience / Circoloco Phuket weeks | Club/party series |
| Waterbomb Manila | K-pop water fest, not our genres |

Manual curation only. No scraping of Ticketmelon, Eventpop, Megatix, or Insomniac beyond using those pages as **human** sources of truth.

---

## 14. Japan research notes (13 Sep 2026)

**Short answer:** Japan has **good stuff**, especially boutique house/techno. It does **not** currently have a dense dated 2026/27 catalog like the Netherlands or even Thailand.

**What is actually good**

- **Labyrinth** (Mindgames) is the closest JP analogue to Soenda/Draaimolen: English official site, camping, music windows, promised week-of timetable. Genre fit is high. Clash fit is **low** if it stays one Funktion-One floor.
- **Rainbow Disco Club** (Higashi-Izu) and **rural** (Fukushima nowhere CAMP) are historically the two outdoor JP events we’d want for Fit + later Clash. Both **2026 editions are already over**; **2027 dates are not on the official sites**.
- **Snow Machine Hakuba 2027** is the rare JP 2027 poster with **Wilkinson / Kanine** (DnB) plus house/techno. Maintainability is ski-holiday HARD.
- **Ultra Japan 2026** is the only JP event with **named stages + an official running-order image right now**. It is also **this weekend** (19–20 Sep) and EDM-first. Resistance (Sara Landry, Lilly Palmer, Ken Ishii) is the only tent that matches our brief tightly.

**What looks big and is still the wrong object**

- Fuji Rock, Summer Sonic, Rising Sun, Asagiri Jam: Japanese festival culture, not an electronic catalog.
- GMO SONIC: arena EDM; dates yes, artists no.
- Tokyo DnB (WOMB 06S, ZEROTOKYO, Rampage Roadshow): real scene, **club nights**. Rampage Tokyo Vol.2 was 30 May 2026.

**Compared with other regions**

| Region | Genre taste | Official 2026/27 pages | Clash this window |
|---|---|---|---|
| Netherlands / CZ / BE | Best | Best | Best |
| Thailand 2026 | Mixed EDM + some techno/DnB | Strong (EDC, 808, TML) | 808 maybe |
| Japan | Best boutique techno/house in Asia | Thin until RDC/rural 2027 drop | Ultra PNG this weekend; Labyrinth maybe 1 floor |
| India | Weak | Weak | No |

Add Japan shells. Do not rebuild V1 around Tokyo.
