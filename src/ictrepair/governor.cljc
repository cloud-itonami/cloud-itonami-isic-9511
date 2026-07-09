(ns ictrepair.governor
  "Repair Governor -- the independent compliance layer that earns the
  RepairOps-LLM the right to commit. The LLM has no notion of
  jurisdictional consumer-product-safety/media-sanitization law,
  whether a claimed parts cost actually equals the ticket's own
  parts-quantity times parts-unit-price, whether a device has actually
  passed its post-repair safety test, whether a removed storage
  component's data has actually been securely sanitized, or when an
  act stops being a draft and becomes a real-world repair completion
  or device return, so this MUST be a separate system able to *reject*
  a proposal and fall back to HOLD -- the ICT-equipment-repair analog
  of `cloud-itonami-isic-9521`'s `repairshop.governor` (itself the
  electronics-repair analog of `cloud-itonami-isic-6512`'s
  `casualty.governor`), and structurally closest to `cloud-itonami-
  isic-9523`'s `leathergoods.governor` (repair of footwear and leather
  goods).

  Unlike every other actor in the `:repair-shop-governor` family
  (`repairshop`/9521, `commrepair`/9512, `applianceshop`/9522,
  `furniture`/9524, `specialtyrepair`/9529, `leathergoods`/9523), this
  blueprint's own `:itonami.blueprint/governor` keyword is
  `:repair-governor` -- grep-verified as a UNIQUE keyword fleet-wide
  (no other blueprint declares it), so there is no governor-name-reuse
  precedent question here at all: this is simply a fresh, independent
  build, following the SAME governed-actor architecture (langgraph
  StateGraph + independent Governor + Phase 0->3 rollout) established
  by `cloud-itonami-isic-6511` and reused across every prior actor in
  this fleet, but with its own distinct governor identity.

  This blueprint's own named business (community ICT equipment repair
  -- computers, servers, laptops, storage arrays) routinely involves
  REPLACING or REMOVING a data-bearing storage component during
  repair, a genuinely distinct real-world concern none of `repairshop`/
  9521's, `commrepair`/9512's, `applianceshop`/9522's, `furniture`/
  9524's, `specialtyrepair`/9529's nor `leathergoods`/9523's own
  catalogs model -- see Decision 5 below, and note this is STRUCTURALLY
  DIFFERENT from `commrepair`/9512's own `customer-data-consent-
  unconfirmed?` check (consent to ACCESS data DURING repair, evaluated
  unconditionally) -- this check is about secure DESTRUCTION of a
  REMOVED storage component AFTER the repair decision to replace it,
  evaluated CONDITIONALLY on the ticket's own storage-replacement
  ground truth.

  Seven checks, in priority order, ALL HARD violations: a human
  approver CANNOT override them (you don't get to approve your way
  past a fabricated jurisdiction spec-basis, incomplete repair
  evidence, a parts-cost claim that doesn't match quantity times unit-
  price, a device returned without a passed safety test, an unconfirmed
  media-sanitization status for a replaced storage component, or a
  double completion/return). The confidence/actuation gate is SOFT: it
  asks a human to look (low confidence / actuation), and the human may
  approve -- but see `ictrepair.phase`: for `:stake :actuation/
  complete-repair`/`:actuation/return-device` (a real repair completion
  or device return) NO phase ever allows auto-commit either. Two
  independent layers agree that actuation is always a human call.

    1. Spec-basis                  -- did the jurisdiction proposal cite
                                       an OFFICIAL source
                                       (`ictrepair.facts`), or invent
                                       one?
    2. Evidence incomplete         -- for `:repair/complete`/`:device/
                                       return`, has the jurisdiction
                                       actually been assessed with a
                                       full repair-evidence checklist
                                       on file?
    3. Parts cost mismatch         -- for `:repair/complete`,
                                       INDEPENDENTLY recompute whether
                                       the ticket's own `:claimed-
                                       parts-cost` equals `parts-
                                       quantity x parts-unit-price`
                                       (`ictrepair.registry/
                                       parts-cost-matches-claim?`) --
                                       an HONEST, literal reuse of
                                       `repairshop.registry`'s/
                                       `commrepair.registry`'s/
                                       `applianceshop.registry`'s/
                                       `furniture.registry`'s/
                                       `specialtyrepair.registry`'s/
                                       `leathergoods.registry`'s own
                                       EXACT-MATCH independent-
                                       recompute check for the SAME
                                       real-world concern, not claimed
                                       as new.
    4. Safety test not passed      -- for `:device/return`, reported by
                                       THIS proposal itself (a
                                       `:safety/screen` that just found
                                       a failed test), or already on
                                       file for the ticket (`:safety/
                                       screen`/`:device/return`).
                                       Evaluated UNCONDITIONALLY (not
                                       scoped to a specific op) -- an
                                       HONEST, literal reuse of
                                       `repairshop.governor`'s/
                                       `commrepair.governor`'s/
                                       `applianceshop.governor`'s/
                                       `furniture.governor`'s/
                                       `specialtyrepair.governor`'s/
                                       `leathergoods.governor`'s own
                                       check for the SAME real-world
                                       concern (post-repair safety
                                       testing -- e.g. a repaired power
                                       supply must not pose a shock/
                                       fire risk), not claimed as new.
    5. Media sanitization
       unconfirmed                    -- for a ticket whose own record
                                       declares `:involves-storage-
                                       replacement? true` (i.e. this
                                       repair actually replaced/removed
                                       a data-bearing storage component
                                       -- not every ICT repair does),
                                       INDEPENDENTLY check whether
                                       `:media-sanitization-confirmed?`
                                       is true. A GENUINELY NEW concept
                                       (grep-verified absent fleet-wide
                                       -- zero hits for 'media-
                                       sanitiz'/'secure-disposal'/
                                       '800-88'/'data-destruction' as a
                                       governor CHECK function name;
                                       ALSO grep-verified DISTINCT from
                                       `commrepair.governor/customer-
                                       data-consent-unconfirmed-
                                       violations`, which addresses a
                                       different real-world moment --
                                       consent to access, not secure
                                       destruction of removed media),
                                       the 70th distinct application of
                                       the unconditional-evaluation
                                       discipline overall (most
                                       recently `leathergoods.governor/
                                       brand-authenticity-unconfirmed-
                                       violations` at 69th), the
                                       SEVENTH conditional variant
                                       (after `socialresearch`/7220's,
                                       `bizassoc`/9411's, `training`/
                                       8549's, `furniture`/9524's,
                                       `specialtyrepair`/9529's and
                                       `leathergoods`/9523's own, at
                                       63rd, 64th, 66th, 67th, 68th and
                                       69th). CONDITIONAL on the
                                       ticket's own `:involves-storage-
                                       replacement?` ground truth -- a
                                       screen/battery repair has no
                                       media-sanitization concern at
                                       all. Grounded in real media-
                                       sanitization/data-destruction
                                       law: NIST SP 800-88 Rev.1
                                       (Guidelines for Media
                                       Sanitization, enforced via the
                                       US FTC's FACTA Disposal Rule,
                                       16 C.F.R. Part 682), UK GDPR
                                       Article 5(1)(f)/Article 32
                                       (enforced by the ICO), Germany's
                                       DSGVO Art. 32 / BDSG (enforced by
                                       the BfDI), and Japan's own
                                       個人情報保護法 (APPI) 安全管理措置
                                       requirements (enforced by 個人情報
                                       保護委員会) -- unlike some prior
                                       repair-shop-cluster siblings' own
                                       honest single-jurisdiction gap,
                                       ALL FOUR seeded jurisdictions
                                       actually have a real media-
                                       sanitization/data-destruction
                                       enforcement regime here, reported
                                       honestly (matching `leathergoods`/
                                       9523's own full-coverage brand-
                                       authenticity sub-citation).
    6. Confidence floor / actuation
       gate                          -- LLM confidence below threshold,
                                       OR the op is `:repair/complete`/
                                       `:device/return` (REAL acts) ->
                                       escalate.

  Two more guards, double-completion/double-return prevention, are
  enforced but NOT listed as numbered HARD checks above because they
  need no upstream comparison at all -- `already-completed-violations`/
  `already-returned-violations` refuse to complete/return the SAME
  ticket twice, off dedicated `:repair-completed?`/`:device-returned?`
  facts (never a `:status` value) -- the SAME 'check a dedicated
  boolean, not status' discipline every prior governor's guards
  establish, informed by `cloud-itonami-isic-6492`'s status-lifecycle
  bug (ADR-2607071320)."
  (:require [ictrepair.facts :as facts]
            [ictrepair.registry :as registry]
            [ictrepair.store :as store]))

(def confidence-floor 0.6)

(def high-stakes
  "Stakes grave enough to always require a human, even when clean.
  Completing a real repair and returning a real device are the two
  real-world actuation events this actor performs -- a two-member set,
  matching `repairshop`/9521's, `commrepair`/9512's, `applianceshop`/
  9522's, `furniture`/9524's, `specialtyrepair`/9529's and
  `leathergoods`/9523's own dual-actuation shape."
  #{:actuation/complete-repair :actuation/return-device})

;; ----------------------------- checks -----------------------------

(defn- spec-basis-violations
  "A `:jurisdiction/assess` (or `:repair/complete`/`:device/return`)
  proposal with no spec-basis citation is a HARD violation -- never
  invent a jurisdiction's consumer-product-safety/media-sanitization
  requirements."
  [{:keys [op]} proposal]
  (when (contains? #{:jurisdiction/assess :repair/complete :device/return} op)
    (let [value (:value proposal)]
      (when (or (empty? (:cites proposal))
                (and (contains? value :spec-basis) (nil? (:spec-basis value))))
        [{:rule :no-spec-basis
          :detail "公式spec-basisの引用が無い提案は法域要件として扱えない"}]))))

(defn- evidence-incomplete-violations
  "For `:repair/complete`/`:device/return`, the jurisdiction's required
  diagnostic/parts-used/safety-test/media-sanitization evidence must
  actually be satisfied -- do not trust the advisor's self-reported
  confidence alone."
  [{:keys [op subject]} st]
  (when (contains? #{:repair/complete :device/return} op)
    (let [t (store/ticket st subject)
          assessment (store/assessment-of st subject)]
      (when-not (and assessment
                     (facts/required-evidence-satisfied?
                      (:jurisdiction t) (:checklist assessment)))
        [{:rule :evidence-incomplete
          :detail "法域の必要書類(故障診断書/使用部品記録/安全試験記録/記憶媒体消去証明記録等)が充足していない状態での提案"}]))))

(defn- parts-cost-mismatch-violations
  "For `:repair/complete`, INDEPENDENTLY recompute whether the
  ticket's own claimed parts cost equals parts-quantity x parts-unit-
  price via `ictrepair.registry/parts-cost-matches-claim?` -- needs no
  proposal inspection or stored-verdict lookup at all, an honest reuse
  of `repairshop.registry`'s/`commrepair.registry`'s/`applianceshop.
  registry`'s/`furniture.registry`'s/`specialtyrepair.registry`'s/
  `leathergoods.registry`'s own check."
  [{:keys [op subject]} st]
  (when (= op :repair/complete)
    (let [t (store/ticket st subject)]
      (when-not (registry/parts-cost-matches-claim? t)
        [{:rule :parts-cost-mismatch
          :detail (str subject " の申告部品代金(" (:claimed-parts-cost t)
                      ")が独立再計算値(" (registry/compute-parts-cost t) ")と一致しない")}]))))

(defn- safety-test-not-passed-violations
  "A not-passed post-repair safety test -- reported by THIS proposal
  (e.g. a `:safety/screen` that itself just found a failure), or
  already on file for the ticket (`:safety/screen`/`:device/return`) --
  is a HARD, un-overridable hold. Evaluated UNCONDITIONALLY (not
  scoped to a specific op) so the screening op itself can HARD-hold on
  its own finding."
  [{:keys [op subject]} proposal st]
  (let [hit-in-proposal? (= :failed (get-in proposal [:value :verdict]))
        ticket-id (when (contains? #{:safety/screen :device/return} op) subject)
        hit-on-file? (and ticket-id (= :failed (:verdict (store/safety-screening-of st ticket-id))))]
    (when (or hit-in-proposal? hit-on-file?)
      [{:rule :safety-test-not-passed
        :detail "修理後安全試験に合格していない機器を返却する提案は進められない"}])))

(defn- media-sanitization-unconfirmed-violations
  "For a ticket whose own record declares `:involves-storage-
  replacement? true`, INDEPENDENTLY check whether `:media-
  sanitization-confirmed?` is true -- a genuinely new concept (see ns
  docstring), CONDITIONAL on the ticket's own `:involves-storage-
  replacement?` ground truth (a repair that does not replace/remove a
  data-bearing storage component has no media-sanitization requirement
  at all). Scoped to `:media/screen` and `:repair/complete`/`:device/
  return`, so the screening op itself can HARD-hold on its own
  finding, matching every prior unconditional-evaluation check's
  scoping shape."
  [{:keys [op subject]} st]
  (when (contains? #{:media/screen :repair/complete :device/return} op)
    (let [t (store/ticket st subject)]
      (when (and (true? (:involves-storage-replacement? t))
                 (not (true? (:media-sanitization-confirmed? t))))
        [{:rule :media-sanitization-unconfirmed
          :detail (str subject " は記憶媒体の交換を伴うが消去証明が未完了 -- 修理完了/返却提案は進められない")}]))))

(defn- already-completed-violations
  "For `:repair/complete`, refuses to complete the SAME ticket's
  repair twice, off a dedicated `:repair-completed?` fact (never a
  `:status` value)."
  [{:keys [op subject]} st]
  (when (= op :repair/complete)
    (when (store/ticket-already-completed? st subject)
      [{:rule :already-completed
        :detail (str subject " は既に修理完了済み")}])))

(defn- already-returned-violations
  "For `:device/return`, refuses to return the SAME ticket's device
  twice, off a dedicated `:device-returned?` fact (never a `:status`
  value)."
  [{:keys [op subject]} st]
  (when (= op :device/return)
    (when (store/ticket-already-returned? st subject)
      [{:rule :already-returned
        :detail (str subject " は既に返却済み")}])))

(defn check
  "Censors a RepairOps-LLM proposal against the governor rules.
  Returns {:ok? bool :violations [..] :confidence c :escalate? bool
  :high-stakes? bool :hard? bool}."
  [request _context proposal st]
  (let [hard (into []
                   (concat (spec-basis-violations request proposal)
                           (evidence-incomplete-violations request st)
                           (parts-cost-mismatch-violations request st)
                           (safety-test-not-passed-violations request proposal st)
                           (media-sanitization-unconfirmed-violations request st)
                           (already-completed-violations request st)
                           (already-returned-violations request st)))
        conf (:confidence proposal 0.0)
        low? (< conf confidence-floor)
        stakes? (boolean (high-stakes (:stake proposal)))
        hard? (boolean (seq hard))]
    {:ok?          (and (not hard?) (not low?) (not stakes?))
     :violations   hard
     :confidence   conf
     :hard?        hard?
     :escalate?    (and (not hard?) (or low? stakes?))
     :high-stakes? stakes?}))

(defn hold-fact
  "The audit fact written when a proposal is rejected (HOLD)."
  [request context verdict]
  {:t          :governor-hold
   :op         (:op request)
   :actor      (:actor-id context)
   :subject    (:subject request)
   :disposition :hold
   :basis      (mapv :rule (:violations verdict))
   :violations (:violations verdict)
   :confidence (:confidence verdict)})
