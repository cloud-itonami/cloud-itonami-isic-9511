# ADR-0001: RepairOps-LLM ⊣ Repair Governor architecture

## Status

Accepted. `cloud-itonami-isic-9511` promoted from `:blueprint` to
`:implemented` in the `kotoba-lang/industry` registry.

## Context

`cloud-itonami-isic-9511` publishes an OSS business blueprint for
community ICT equipment repair (computers, servers, laptops, storage
arrays). Like every prior actor in this fleet, the blueprint alone is
not an implementation: this ADR records the governed-actor
architecture that promotes it to real, tested code, following the
same langgraph StateGraph + independent Governor + Phase 0→3 rollout
pattern established by `cloud-itonami-isic-6511` (life insurance) and
applied across 85 prior siblings, most recently `cloud-itonami-isic-
9523` (repair of footwear and leather goods).

This build is the first in a newly-approved extension of scope: after
completing the entire `cloud-itonami/cloud-itonami-isic-*` blueprint
fleet whose `:repo` fields correctly pointed at that org, a separate
set of 13 registry entries was discovered whose `:repo` fields still
pointed at the STALE `gftdcojp/cloud-itonami-<id>` location (the repos
had been renamed/transferred to `cloud-itonami/cloud-itonami-isic-
<id>`, same as every other repo in this fleet, but the registry never
caught up). That stale-URL data was corrected in a dedicated `kotoba-
lang/industry` commit prior to this build. Of the 13, one (4211,
Community Building Construction) is already partially implemented;
the other 12, including this one, are genuine `:blueprint`-tier
candidates with no existing code.

Unlike the `:repair-shop-governor` family (`repairshop`/9521,
`commrepair`/9512, `applianceshop`/9522, `furniture`/9524,
`specialtyrepair`/9529, `leathergoods`/9523), this blueprint's own
`:itonami.blueprint/governor` keyword, `:repair-governor`, is a UNIQUE
keyword fleet-wide (grep-verified: no other blueprint declares it).
There is therefore no governor-name-reuse precedent question here --
this is simply a fresh, independent build following the same
architecture pattern.

## Decision

### Decision 1: fresh governor identity, no reuse precedent needed

`:repair-governor` is grep-verified unique across every blueprint.edn
in this fleet. This build follows the SAME governed-actor architecture
(langgraph StateGraph + independent Governor + Phase 0→3 rollout) as
every prior actor, but with its own distinct governor identity -- no
naming-collision documentation is required.

### Decision 2: dual-actuation shape

This blueprint's own README and business-model.md name two real-world
acts implicitly through its Core Contract diagram ("robot actions
(gated) + operating records + audit ledger", "No automated advice can
... suppress an operating record, or disclose sensitive data without
governor approval") and its `operating-states` (`:intake :diagnose
:quote :repair :return :audit`, per the `kotoba-lang/industry`
registry entry) -- completing a repair and returning a device.
Matching `repairshop`/9521's, `commrepair`/9512's, `applianceshop`/
9522's, `furniture`/9524's, `specialtyrepair`/9529's and
`leathergoods`/9523's own dual-actuation shape, `high-stakes` here is
a two-member set, `#{:actuation/complete-repair :actuation/return-device}`.
`:device/return` (not `:item/return`) matches `commrepair`/9512's own
naming convention for equipment, more natural for ICT devices than
the generic `:item/return` used by the furniture/leathergoods
siblings.

### Decision 3: `parts-cost-matches-claim?` and `safety-test-not-passed?` -- honest, literal reuses

`ictrepair.registry/parts-cost-matches-claim?` and `ictrepair.
governor/safety-test-not-passed-violations` are HONEST, LITERAL reuses
of `repairshop.registry`'s/`commrepair.registry`'s/`applianceshop.
registry`'s/`furniture.registry`'s/`specialtyrepair.registry`'s/
`leathergoods.registry`'s own EXACT-MATCH independent-recompute
checks -- NOT claimed as new. An ICT-equipment-repair ticket's own
claimed parts cost (storage drives, power supplies, motherboards,
displays) against quantity-times-unit-price, and a post-repair safety
test (e.g. a repaired power supply must not pose a shock/fire risk),
are the SAME real-world concerns as `leathergoods`/9523's own checks,
reapplied to a different repair-item category.

### Decision 4: entity and op shape

The primary entity is a `ticket`. Six ops: `:ticket/intake` (directory
upsert, no capital risk), `:jurisdiction/assess` (per-jurisdiction
consumer-product-safety/media-sanitization evidence checklist, never
auto), `:safety/screen` (post-repair safety screening, honest reuse,
never auto), `:media/screen` (media-sanitization screening, GENUINELY
NEW, never auto), `:repair/complete` (POSITIVE, high-stakes), and
`:device/return` (POSITIVE, high-stakes).

### Decision 5: `media-sanitization-unconfirmed?` -- the 70th unconditional-evaluation grounding, a genuinely new concept, DISTINCT from commrepair/9512's own data-privacy check, the SEVENTH conditional variant

Before writing this check, every prior sibling's governor namespace
across the entire fleet was grepped for any check function named
`media-sanitiz`, `secure-disposal`, `800-88` or `data-destruction` --
zero hits, confirming this is a genuinely new concept. Separately,
`commrepair`/9512's own governor was read in full: it already has a
`customer-data-consent-unconfirmed-violations` check, grounded in the
FTC's "Nixing the Fix" report, GDPR, DSGVO and 個人情報保護法. That
check addresses a DIFFERENT real-world moment -- whether the customer
CONSENTED to the repair technician ACCESSING personal data stored on
the device DURING the repair, evaluated UNCONDITIONALLY. This new
check, `media-sanitization-unconfirmed-violations`, addresses secure
DESTRUCTION of a REMOVED storage component AFTER the repair decision
to replace it -- a distinct moment, a distinct duty, and (unlike
`commrepair`/9512's own unconditional check) CONDITIONAL on the
ticket's own `:involves-storage-replacement? true` ground truth: a
screen or battery repair has no media-sanitization concern at all.

`media-sanitization-unconfirmed-violations` reuses the unconditional-
evaluation-screening DISCIPLINE (`casualty.governor/sanctions-
violations`'s original fix) for the 70th distinct application overall
(most recently `leathergoods.governor/brand-authenticity-unconfirmed-
violations` at 69th). This is the SEVENTH conditional variant (after
`socialresearch`/7220's, `bizassoc`/9411's, `training`/8549's,
`furniture`/9524's, `specialtyrepair`/9529's and `leathergoods`/9523's
own, at 63rd, 64th, 66th, 67th, 68th and 69th). Grounded in real
media-sanitization/data-destruction law: NIST SP 800-88 Rev.1
(Guidelines for Media Sanitization, enforced via the US FTC's FACTA
Disposal Rule, 16 C.F.R. Part 682), UK GDPR Article 5(1)(f)/Article 32
(enforced by the ICO), Germany's DSGVO Art. 32 / BDSG (enforced by the
BfDI), and Japan's own 個人情報保護法 (APPI) 安全管理措置 requirements
(enforced by 個人情報保護委員会). Unlike some prior repair-shop-cluster
siblings' own honest single-jurisdiction gap, ALL FOUR seeded
jurisdictions actually have a real media-sanitization/data-destruction
enforcement regime here, reported honestly (matching `leathergoods`/
9523's own full-coverage brand-authenticity sub-citation).

### Decision 6: dedicated double-actuation-guard booleans

`:repair-completed?`/`:device-returned?` are dedicated booleans on the
`ticket` record, never a single `:status` value -- an honest, literal
reuse of `leathergoods.governor`'s own guards, informed by `cloud-
itonami-isic-6492`'s real status-lifecycle bug (ADR-2607071320).

### Decision 7: Store protocol, MemStore + DatomicStore parity

`ictrepair.store/Store` is implemented by both `MemStore` (atom-
backed, default for dev/tests/demo) and `DatomicStore` (`langchain.
db`-backed), proven to satisfy the same contract in
`test/ictrepair/store_contract_test.clj` -- the same seam every
sibling actor uses so swapping the SSoT backend is a configuration
change, not a rewrite.

### Decision 8: Phase 0→3 rollout

Phase 3's `:auto` set has exactly one member, `:ticket/intake` (no
capital risk). `:jurisdiction/assess`, `:safety/screen` and `:media/
screen` are never auto-eligible at any phase (matching every sibling's
screening-op posture), and `:repair/complete`/`:device/return` are
permanently excluded from every phase's `:auto` set -- a structural
fact, not a rollout milestone, enforced by BOTH `ictrepair.phase` and
`ictrepair.governor`'s `high-stakes` set independently.

### Decision 9: no bespoke domain capability lib, and no `blueprint.edn` field-sync fixes needed

This blueprint's own `:itonami.blueprint/required-technologies` names
no domain-specific capability beyond the generic robotics/identity/
forms/dmn/bpmn/audit-ledger stack -- there was no capability-lib
decision to make at all. This repo's `blueprint.edn` already had the
correct `:required-technologies` matching the `kotoba-lang/industry`
registry's own entry for `"9511"` exactly (its own `:optional-
technologies [:optimization]` is registry-only, not required for
readiness) -- only the `:maturity` field itself needed adding.

### Decision 10: mock + LLM advisor pair

`ictrepair.repairopsllm` provides `mock-advisor` (deterministic,
default everywhere -- the actor graph and governor contract run
offline) and `llm-advisor` (backed by `langchain.model/ChatModel`,
with a defensive EDN-proposal parser so a malformed LLM response
degrades to a safe low-confidence noop rather than ever auto-
completing a repair or auto-returning a device).

## Alternatives considered

- **Reusing `commrepair`/9512's own `customer-data-consent-
  unconfirmed?` check verbatim.** Rejected: that check addresses
  consent to ACCESS data during repair (unconditional); this vertical's
  own distinguishing concern is secure DESTRUCTION of a REMOVED
  storage component (conditional on storage replacement) -- a
  genuinely different real-world moment and duty, not a rehash.
- **An unconditional media-sanitization check** (applying to every
  ticket regardless of whether the repair actually replaces a storage
  component). Rejected: a screen or battery repair has no media-
  sanitization concern at all -- forcing the check onto every ticket
  would fabricate a requirement.
- **Fabricating a jurisdiction gap** to match the pattern of some
  prior siblings' own single-jurisdiction honesty gap. Rejected: the
  same honesty discipline that forbids fabricating coverage also
  forbids under-reporting it -- all four seeded jurisdictions
  genuinely have a real media-sanitization/data-destruction regime.

## Consequences

- 86th actor in this fleet (85 implemented before this build).
- First build in the newly-approved gftdcojp-origin-registry
  extension of scope (12 remaining genuine `:blueprint`-tier
  candidates after this one, plus one already-partial entry, 4211,
  noted for separate follow-up).
- Establishes a genuinely NEW conditional unconditional-evaluation-
  screening concept (media-sanitization-unconfirmed?), explicitly
  distinguished from `commrepair`/9512's own data-privacy check,
  grep-verified absent from every prior sibling before the claim was
  finalized.
- `MemStore` ‖ `DatomicStore` parity is proven by
  `test/ictrepair/store_contract_test.clj`, the same `:db-api`-driven
  swap pattern every sibling actor uses.
- 40 tests / 192 assertions pass; lint is clean; the demo
  (`clojure -M:dev:run`) walks one clean dual-actuation lifecycle plus
  five HARD-hold scenarios end-to-end.
- `blueprint.edn` required no field-sync fixes this time (already
  correct) -- only the `:maturity` flip itself.

## References

- `cloud-itonami-isic-9521/docs/adr/0001-architecture.md` (origin of
  the dual-actuation Repair Shop Governor shape, general pattern)
- `cloud-itonami-isic-9512/docs/adr/0001-architecture.md`
  (`customer-data-consent-unconfirmed?`, the check this build's own
  media-sanitization check is explicitly distinguished from)
- `cloud-itonami-isic-9523/docs/adr/0001-architecture.md` (most recent
  prior sibling, template for this ADR's structure)
- NIST SP 800-88 Rev.1, Guidelines for Media Sanitization (US)
- FACTA Disposal Rule, 16 C.F.R. Part 682 (US)
- UK GDPR Article 5(1)(f) / Article 32 (UK)
- DSGVO Art. 32; Bundesdatenschutzgesetz (BDSG) (Germany)
- 個人情報の保護に関する法律 (APPI) 安全管理措置 (Japan)
