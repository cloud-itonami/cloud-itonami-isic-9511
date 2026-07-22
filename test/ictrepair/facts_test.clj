(ns ictrepair.facts-test
  (:require [clojure.test :refer [deftest is]]
            [ictrepair.facts :as facts]))

(deftest jpn-has-a-spec-basis
  (is (some? (facts/spec-basis "JPN")))
  (is (string? (:provenance (facts/spec-basis "JPN")))))

(deftest all-four-seeded-jurisdictions-have-a-media-spec-basis
  ;; unlike some prior repair-shop-cluster siblings' own honest single-
  ;; jurisdiction gap, ALL FOUR seeded jurisdictions actually have a
  ;; real media-sanitization/data-destruction enforcement regime here
  ;; -- reported honestly, not forced narrower
  (doseq [iso3 ["JPN" "USA" "GBR" "DEU"]]
    (is (some? (facts/media-spec-basis iso3)) (str iso3 " media-spec-basis"))
    (is (string? (:media-provenance (facts/media-spec-basis iso3))) (str iso3 " media-provenance"))))

(deftest prt-has-a-spec-basis-with-the-same-shape-as-the-other-four
  ;; PRT (Portugal) added as the 5th jurisdiction -- same 9-key shape as
  ;; JPN/USA/GBR/DEU: :name/:owner-authority/:legal-basis/:national-spec/
  ;; :provenance/:required-evidence (PLUS the 3 :media-* keys asserted in
  ;; the next test below).
  (let [prt (facts/spec-basis "PRT")]
    (is (some? prt))
    (is (= "Portugal" (:name prt)))
    (is (string? (:owner-authority prt)))
    (is (string? (:legal-basis prt)))
    (is (string? (:national-spec prt)))
    (is (string? (:provenance prt)))
    (is (= #{:name :owner-authority :legal-basis :national-spec :provenance
             :required-evidence :media-owner-authority :media-legal-basis
             :media-provenance}
           (set (keys prt)))
        "PRT must have the exact same key set as JPN/USA/GBR/DEU")
    (is (= 4 (count (:required-evidence prt)))
        "PRT's required-evidence must mirror the generic 4-item evidence set")))

(deftest prt-has-a-media-spec-basis
  ;; unlike the ambiguous REEE "produtor" question disclosed in a
  ;; catalog comment (which has no key of its own in this 9-key shape),
  ;; PRT's media-sanitization/data-destruction spec-basis (GDPR/RGPD,
  ;; enforced by the CNPD) IS confirmed, same as the other four
  (is (some? (facts/media-spec-basis "PRT")))
  (is (string? (:media-provenance (facts/media-spec-basis "PRT"))))
  (is (= "Comissão Nacional de Proteção de Dados (CNPD)"
         (:media-owner-authority (facts/media-spec-basis "PRT")))))

(deftest unknown-jurisdiction-has-no-fabricated-spec-basis
  (is (nil? (facts/spec-basis "ATL"))))

(deftest unknown-jurisdiction-has-no-media-spec-basis
  (is (nil? (facts/media-spec-basis "ATL"))))

(deftest coverage-never-reports-a-missing-jurisdiction-as-covered
  (let [report (facts/coverage ["JPN" "ATL" "GBR"])]
    (is (= 2 (:covered report)))
    (is (= ["ATL"] (:missing-jurisdictions report)))
    (is (= ["GBR" "JPN"] (:covered-jurisdictions report)))))

(deftest required-evidence-satisfied-needs-every-item
  (let [all (facts/evidence-checklist "JPN")]
    (is (facts/required-evidence-satisfied? "JPN" all))
    (is (not (facts/required-evidence-satisfied? "JPN" (rest all))))
    (is (not (facts/required-evidence-satisfied? "ATL" all)) "no spec-basis -> never satisfied")))
