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
  `leathergoods`/9523's own brand-authenticity sub-citation, ALL FOUR
  seeded jurisdictions actually have a real media-sanitization/data-
  destruction enforcement regime here, reported honestly rather than
  forcing an artificial gap.")

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
          :media-provenance "https://www.bfdi.bund.de/DE/Buerger/Inhalte/1_Grundlagen/Grundlagen.html"}})

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
  In this R0 catalog all four seeded jurisdictions actually have one
  (unlike some prior siblings' own honest single-jurisdiction gap),
  reported honestly."
  [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:media-owner-authority sb)
      (select-keys sb [:media-owner-authority :media-legal-basis :media-provenance]))))
