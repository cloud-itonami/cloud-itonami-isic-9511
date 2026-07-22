(ns ictrepair.facts
  "Per-jurisdiction consumer-product-safety AND secure-media-
  sanitization/data-destruction regulatory catalog -- the G2-style
  spec-basis table the Repair Governor checks every `:jurisdiction/
  assess` proposal against ('did the advisor cite an OFFICIAL public
  source for this jurisdiction's requirements, or did it invent
  one?'), closely modeled on `cloud-itonami-isic-9523`'s
  `leathergoods.facts`.

  ICT equipment repair (computers, servers, laptops, storage arrays)
  routinely involves REPLACING or REMOVING a data-bearing storage
  component (a failed hard drive, SSD, or similar media) during
  repair -- a real, distinct regulatory concern beyond `repairshop`/
  9521's, `commrepair`/9512's, `applianceshop`/9522's, `furniture`/
  9524's, `specialtyrepair`/9529's and `leathergoods`/9523's own
  catalogs: once a storage component is decommissioned it must be
  SECURELY SANITIZED (or physically destroyed) before disposal/resale,
  since the customer's personal data may still be fully recoverable
  from it otherwise. This is a DIFFERENT concern from `commrepair`/
  9512's own `customer-data-consent-unconfirmed?` check (whether the
  customer consented to the TECHNICIAN accessing data DURING the
  repair) -- this check is about secure DESTRUCTION of a REMOVED
  storage component AFTER the repair decision to replace it, grounded
  in NIST SP 800-88 Rev.1 (Guidelines for Media Sanitization, the de
  facto US technical standard, enforced via the FTC's FACTA Disposal
  Rule, 16 C.F.R. Part 682), UK GDPR Article 5(1)(f)/Article 32
  (integrity/confidentiality of processing, including secure disposal,
  enforced by the ICO), Germany's DSGVO Art. 32 / BDSG (enforced by the
  BfDI and state data-protection authorities), and Japan's 個人情報保護法
  (APPI) 安全管理措置 (security control measures) requirements (enforced
  by 個人情報保護委員会, the Personal Information Protection Commission).
  Each jurisdiction entry below therefore cites BOTH the consumer-
  product-safety law this fleet's repair-shop catalogs already model
  AND a SEPARATE media-sanitization/data-destruction law.

  Coverage is reported HONESTLY (see `coverage`), the same discipline
  every sibling actor's `facts` namespace uses: a jurisdiction not in
  this table has NO spec-basis, full stop -- the advisor must not
  fabricate one, and the governor holds if it tries. As with
  `leathergoods`/9523's own brand-authenticity sub-citation, ALL FIVE
  seeded jurisdictions (JPN/USA/GBR/DEU, plus PRT added afterward)
  actually have a real media-sanitization/data-destruction enforcement
  regime here, reported honestly rather than forcing an artificial gap.

  PRT (Portugal) is a partial exception to the otherwise-uniform
  'consumer-product-safety law' pattern of the other four entries: as
  an EU member state, Portugal's directly-applicable, non-transposed
  General Product Safety Regulation ((EU) 2023/988, GPSR) fills the
  `:legal-basis`/`:national-spec` role (GPSR needs no national
  transposition -- it has applied directly since 2024-12-13, verified
  via its own Art. 52), enforced by ASAE (Autoridade de Segurança
  Alimentar e Económica). Three additional PRT-specific research
  findings from the EU/national right-to-repair and e-waste angle did
  NOT fit this catalog's 9-key shape and are instead recorded as
  in-map comments next to the `\"PRT\"` entry below, in the same
  'disclose the gap rather than fabricate' spirit as everything else
  in this namespace: (1) the EU right-to-repair reform (Directive (EU)
  2024/1799) was, as of this entry's research, NOT YET transposed into
  Portuguese law (EUR-Lex's own National Transposition Measures page
  showed zero notified PRT measures) and its Art. 22(1) transposition
  deadline (2026-07-31) had not yet passed -- so no PRT-specific
  right-to-repair legal-basis is asserted here; (2) the exact Lei de
  Defesa do Consumidor / sale-of-goods decree-law number and any
  specific renewed-warranty-after-repair article could NOT be
  independently confirmed this session (Diário da República's own site
  is a fully client-rendered SPA unreachable via fetch tooling) and is
  therefore left OUT rather than guessed; (3) Portugal's REEE
  (e-waste) extended-producer-responsibility regime (Decreto-Lei n.º
  152-D/2017, as amended/republished by Decreto-Lei n.º 102-D/2020) IS
  confirmed, but whether a PURE repair shop counts as a regulated
  'produtor' under it is a genuine ambiguity -- disclosed, not
  resolved by guessing, the same discipline this fleet applied to a
  prior South-Africa producer/handler ambiguity.")

(def catalog
  "iso3 -> requirement map. `:required-evidence` mirrors the generic
  diagnostic-report/parts-used-documentation/post-repair-safety-test-
  record evidence set (PLUS a media-sanitization record for every
  seeded jurisdiction); `:legal-basis` / `:owner-authority` /
  `:provenance` are the G2 citation the governor requires before any
  `:jurisdiction/assess` proposal can commit. `:media-owner-authority`
  / `:media-legal-basis` / `:media-provenance` are the SEPARATE
  media-sanitization/data-destruction citation the governor's
  `media-sanitization-unconfirmed?` check is grounded in."
  {"JPN" {:name "Japan"
          :owner-authority "経済産業省 (Ministry of Economy, Trade and Industry, METI)"
          :legal-basis "消費生活用製品安全法 (Consumer Product Safety Act)"
          :national-spec "情報機器修理に関する一般消費生活用製品安全基準"
          :provenance "https://www.meti.go.jp/product_safety/"
          :required-evidence ["故障診断書 (diagnostic report)"
                              "使用部品記録 (parts-used documentation)"
                              "修理後安全試験記録 (post-repair safety-test record)"
                              "記憶媒体消去証明記録 (media-sanitization record)"]
          :media-owner-authority "個人情報保護委員会 (Personal Information Protection Commission, PPC)"
          :media-legal-basis "個人情報の保護に関する法律 (APPI) 安全管理措置 (security control measures)"
          :media-provenance "https://www.ppc.go.jp/personalinfo/legal/"}
   "USA" {:name "United States"
          :owner-authority "U.S. Consumer Product Safety Commission (CPSC)"
          :legal-basis "Consumer Product Safety Act (15 U.S.C. §§2051 et seq.)"
          :national-spec "CPSC product-safety standards for personal/household goods"
          :provenance "https://www.cpsc.gov/Regulations-Laws--Standards/Statutes"
          :required-evidence ["Diagnostic report"
                              "Parts-used documentation"
                              "Post-repair safety-test record"
                              "Media-sanitization record"]
          :media-owner-authority "Federal Trade Commission (FTC)"
          :media-legal-basis "FACTA Disposal Rule (16 C.F.R. Part 682); NIST SP 800-88 Rev.1 Guidelines for Media Sanitization (technical reference standard)"
          :media-provenance "https://www.nist.gov/privacy-framework/nist-sp-800-88"}
   "GBR" {:name "United Kingdom"
          :owner-authority "Office for Product Safety and Standards (OPSS)"
          :legal-basis "General Product Safety Regulations 2005"
          :national-spec "OPSS product-safety enforcement standards for personal/household goods"
          :provenance "https://www.gov.uk/government/organisations/office-for-product-safety-and-standards"
          :required-evidence ["Diagnostic report"
                              "Parts-used documentation"
                              "Post-repair safety-test record"
                              "Media-sanitization record"]
          :media-owner-authority "Information Commissioner's Office (ICO)"
          :media-legal-basis "UK GDPR Article 5(1)(f) / Article 32 (integrity and confidentiality, secure disposal)"
          :media-provenance "https://ico.org.uk/for-organisations/uk-gdpr-guidance-and-resources/"}
   "DEU" {:name "Germany"
          :owner-authority "Marktüberwachungsbehörden der Länder"
          :legal-basis "Produktsicherheitsgesetz (ProdSG)"
          :national-spec "ProdSG Marktüberwachungsanforderungen für Gebrauchsgegenstände"
          :provenance "https://www.baua.de/DE/Themen/Anwendungssichere-Chemikalien-und-Produkte/Produktsicherheit/Produktsicherheit_node.html"
          :required-evidence ["Diagnosebericht (diagnostic report)"
                              "Ersatzteilnachweis (parts-used documentation)"
                              "Sicherheitsprüfungsprotokoll nach Reparatur (post-repair safety-test record)"
                              "Datenträgerlöschungsnachweis (media-sanitization record)"]
          :media-owner-authority "Bundesbeauftragter für den Datenschutz und die Informationsfreiheit (BfDI) / Landesdatenschutzbehörden"
          :media-legal-basis "DSGVO Art. 32 (Sicherheit der Verarbeitung); Bundesdatenschutzgesetz (BDSG)"
          :media-provenance "https://www.bfdi.bund.de/DE/Buerger/Inhalte/1_Grundlagen/Grundlagen.html"}
   ;; PRT (Portugal) -- added after the initial JPN/USA/GBR/DEU seed, R1.
   ;; :legal-basis/:national-spec cite the EU General Product Safety
   ;; Regulation ((EU) 2023/988, GPSR) rather than a Portugal-specific
   ;; product-safety statute: GPSR is a REGULATION, directly applicable in
   ;; every member state without national transposition (verified via its
   ;; own Art. 52: "shall apply from 13 December 2024" -- fetched from
   ;; EUR-Lex CELEX:32023R0988 this session), unlike the national statutes
   ;; the other four entries cite. Enforcement in Portugal is by ASAE
   ;; (Autoridade de Segurança Alimentar e Económica -- name verified
   ;; verbatim from asae.gov.pt's own fetched page content this session);
   ;; ASAE's own site lists RAPEX/ICSMS/CPC/IMI under its
   ;; Inspeção-Fiscalização > "Sistemas de Alerta e Troca de Informação"
   ;; menu (fetched this session), which is strong supporting evidence for
   ;; its Art. 10-of-Regulation-(EU)-2019/1020 market-surveillance-authority
   ;; role for non-food products, but no single fetched source explicitly
   ;; named ASAE as PRT's GPSR-designated authority -- disclosed as
   ;; supporting evidence, not an explicit designation notice.
   ;;
   ;; Three additional research findings (see the `ictrepair.facts` ns
   ;; docstring above for the full disclosure) do NOT have a key in this
   ;; catalog's 9-key shape and are recorded here only as comments, exactly
   ;; because forcing them into :legal-basis/:national-spec would misrepresent
   ;; the map's meaning:
   ;;   1. Right to repair: Directive (EU) 2024/1799 was NOT YET transposed
   ;;      into Portuguese law as of this research (EUR-Lex's National
   ;;      Transposition Measures page, fetched this session, showed PRT
   ;;      with a "noNims" / 0-measures row; the directive's own Art. 22(1)
   ;;      deadline, "Member States shall bring into force the laws,
   ;;      regulations and administrative provisions necessary to comply
   ;;      with this Directive by 31 July 2026 at the latest", had not yet
   ;;      passed at research time) -- so no PRT-specific right-to-repair
   ;;      legal-basis is asserted anywhere in this entry.
   ;;   2. Lei de Defesa do Consumidor / sale-of-goods warranty-on-repair:
   ;;      EUR-Lex's NIM cross-reference for Directive (EU) 2019/771
   ;;      (fetched this session) confirms Portugal's transposing instrument
   ;;      was published in Diário da República I, n.º 202, 2021-10-18, in
   ;;      force since 2021-07-01 -- but the exact decree-law NUMBER and any
   ;;      specific renewed-warranty-after-repair article could NOT be
   ;;      independently confirmed this session (diariodarepublica.pt is a
   ;;      fully client-rendered SPA that returned only an empty
   ;;      `<div id="reactContainer">` shell to both curl and WebFetch;
   ;;      pgdlisboa.pt's own search endpoint returned no results for this
   ;;      law via the accessible GET form). Left out rather than guessed.
   ;;   3. REEE (e-waste): Portugal's extended-producer-responsibility
   ;;      regime is Decreto-Lei n.º 152-D/2017, de 11 de dezembro, "alterado
   ;;      e republicado pelo Decreto-Lei n.º 102-D/2020, de 10 de dezembro"
   ;;      -- confirmed verbatim from the joint APA (Agência Portuguesa do
   ;;      Ambiente) / DGAE (Direção-Geral das Atividades Económicas)
   ;;      "Manual de apoio ao cumprimento do Unilex" (official PDF, fetched
   ;;      this session from apambiente.pt). That manual's own "Produtor"
   ;;      (producer) definition centers on designing/manufacturing/
   ;;      assembling/labelling/importing/rebranding a product and PLACING
   ;;      IT ON THE MARKET -- a pure repair shop that returns a repaired
   ;;      device to its existing owner does not plainly meet this
   ;;      definition for that device. What IS confirmed is that a repair
   ;;      shop, as an "EMPRESA (utilizador não particular)" that comes to
   ;;      HOLD replaced/failed REEE components, is bound by Art. 66.º, n.º
   ;;      1 (duty to route held REEE through individual/integrated
   ;;      management systems or licensed REEE treatment operators) and
   ;;      Art. 61.º, n.º 4, alínea a) (REEE abandonment is prohibited) --
   ;;      per the same manual. Whether ASAE/APA has ever specifically
   ;;      opined on repair-only shops as "produtor" vs. mere REEE "holder"
   ;;      could not be confirmed this session; disclosed as a genuine open
   ;;      question, the same discipline this fleet applied to a prior
   ;;      South-Africa producer/handler ambiguity, rather than resolved by
   ;;      guessing.
   "PRT" {:name "Portugal"
          :owner-authority "ASAE - Autoridade de Segurança Alimentar e Económica"
          :legal-basis "Regulamento (UE) 2023/988 relativo à segurança geral dos produtos (General Product Safety Regulation, GPSR) -- diretamente aplicável em Portugal desde 13 de dezembro de 2024, sem necessidade de transposição nacional (é um regulamento, não uma diretiva)"
          :national-spec "Fiscalização de mercado da ASAE (Inspeção-Fiscalização / Área Económica), participante nos sistemas de alerta RAPEX/Safety Gate, ICSMS, CPC e IMI da UE"
          :provenance "https://www.asae.gov.pt/"
          :required-evidence ["Relatório de diagnóstico (diagnostic report)"
                              "Documentação de peças utilizadas (parts-used documentation)"
                              "Registo de teste de segurança pós-reparação (post-repair safety-test record)"
                              "Registo de eliminação segura de dados do suporte (media-sanitization record)"]
          :media-owner-authority "Comissão Nacional de Proteção de Dados (CNPD)"
          :media-legal-basis "RGPD/GDPR Artigo 5.º, n.º 1, alínea f) (\"integridade e confidencialidade\") / Artigo 32.º (segurança do tratamento) -- Regulamento (UE) 2016/679, diretamente aplicável; complementado a nível nacional pela Lei n.º 58/2019, de 8 de agosto"
          :media-provenance "https://www.cnpd.pt/en/"}})

(defn spec-basis
  "The jurisdiction's requirement map, or nil -- nil means NO spec-basis,
  and the governor must hold any proposal that tries to complete a
  repair or return a device on it."
  [iso3]
  (get catalog iso3))

(defn coverage
  "Honest coverage report: how many of the requested jurisdictions actually
  have a spec-basis entry. Never report a missing jurisdiction as covered."
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-isic-9511 R0: " (count catalog)
                 " jurisdictions seeded with an official spec-basis. "
                 "This is a starting catalog, not a survey of all ~194 "
                 "jurisdictions -- extend `ictrepair.facts/catalog`, "
                 "never fabricate a jurisdiction's requirements.")})))

(defn required-evidence-satisfied?
  "Does `submitted` (a set/coll of evidence keywords or strings) satisfy
  every evidence item listed for `iso3`? Missing spec-basis -> never
  satisfied."
  [iso3 submitted]
  (when-let [{:keys [required-evidence]} (spec-basis iso3)]
    (let [need (count required-evidence)
          have (count (filter (set submitted) required-evidence))]
      (= need have))))

(defn evidence-checklist [iso3]
  (:required-evidence (spec-basis iso3) []))

(defn media-spec-basis
  "The jurisdiction's media-sanitization/data-destruction requirement
  map, or nil -- nil means this jurisdiction has NO formal statutory
  media-sanitization/data-destruction regime this catalog is aware of.
  In this catalog all five seeded jurisdictions actually have one
  (unlike some prior siblings' own honest single-jurisdiction gap),
  reported honestly."
  [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:media-owner-authority sb)
      (select-keys sb [:media-owner-authority :media-legal-basis :media-provenance]))))
