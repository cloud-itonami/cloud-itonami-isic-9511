# cloud-itonami-isic-9511

Open Business Blueprint for **ISIC Rev.5 9511**: Community ICT
Equipment Repair -- intake, diagnosis, quote, repair and return.

This repository publishes an ICT-equipment-repair actor -- item
intake, per-jurisdiction consumer-product-safety/media-sanitization
regulatory assessment, post-repair safety screening, media-
sanitization screening, repair completion and device return -- as an
OSS business that any qualified operator can fork, deploy, run,
improve and sell, so a community repair shop, refurbisher or right-
to-repair program never surrenders customer data and ledgers to a
closed SaaS.

Built on this workspace's
[`langgraph`](https://github.com/kotoba-lang/langgraph)
StateGraph runtime (portable `.cljc`, supervised superstep loop,
interrupts, Datomic/in-mem checkpoints) -- the same actor pattern as
every prior actor in this fleet
([`cloud-itonami-isic-6511`](https://github.com/cloud-itonami/cloud-itonami-isic-6511)
through
[`9529`](https://github.com/cloud-itonami/cloud-itonami-isic-9529)
and
[`9523`](https://github.com/cloud-itonami/cloud-itonami-isic-9523),
85 prior actors in total) -- here it is **RepairOps-LLM ⊣ Repair
Governor**. This blueprint's own `:itonami.blueprint/governor`
keyword, `:repair-governor`, is a UNIQUE keyword fleet-wide (grep-
verified: no other blueprint declares it) -- unlike the
`:repair-shop-governor` family (`repairshop`/9521, `commrepair`/9512,
`applianceshop`/9522, `furniture`/9524, `specialtyrepair`/9529,
`leathergoods`/9523), this is a fresh, independently-named build:
same governed-actor architecture, its own distinct governor identity.

> **Why an actor layer at all?** An LLM is great at drafting a
> diagnostic summary, normalizing records, and checking whether a
> claimed parts cost actually equals the ticket's own recorded parts-
> quantity times unit-price -- but it has **no notion of which
> jurisdiction's consumer-product-safety/media-sanitization law is
> official, no license to complete a real repair or return a real
> device, and no way to know on its own whether a replaced storage
> component's data has actually been securely sanitized**. Letting it
> complete a repair or return a device directly invites fabricated
> regulatory citations, a result being charged on top of a mismatched
> parts-cost claim, a device reaching a customer without a passed
> safety test, and a decommissioned hard drive or SSD reaching a
> disposal stream with the previous customer's personal data still
> fully recoverable -- exposing the shop to real data-protection
> liability -- and liability, for whoever runs it. This project seals
> the RepairOps-LLM into a single node and wraps it with an
> independent **Repair Governor**, a human **approval workflow**, and
> an immutable **audit ledger**.

## Scope: what this actor does and does not do

This actor covers item intake through consumer-product-safety/media-
sanitization regulatory assessment, post-repair safety screening,
media-sanitization screening, repair completion and device return. It
does **not**, by itself, hold any license required to operate an ICT-
equipment-repair shop in a given jurisdiction, and it does not claim
to. It also does not perform the actual repair/diagnostic work
itself, or judge repair quality -- `ictrepair.registry/parts-cost-
matches-claim?` is a pure ground-truth recompute against the ticket's
own recorded fields, not a repair-quality judgment. Whoever deploys
and operates a live instance (a qualified ICT-equipment-repair
technician/shop owner) supplies any jurisdiction-specific license, the
real diagnostic/repair delivery and the real repair-shop-management-
system integrations, and bears that jurisdiction's liability -- the
software supplies the governed, spec-cited, audited execution
scaffold so that operator does not have to build the compliance layer
from scratch.

### Actuation

**Completing a real repair and returning a real device to the
customer are never autonomous, at any phase, by construction.** Two
independent layers enforce this (`ictrepair.governor`'s `:actuation/
complete-repair`/`:actuation/return-device` high-stakes gate and
`ictrepair.phase`'s phase table, which never puts either op in any
phase's `:auto` set) -- see `ictrepair.phase`'s docstring and
`test/ictrepair/phase_test.clj`'s `repair-complete-never-auto-at-any-
phase`/`device-return-never-auto-at-any-phase`. The actor may draft,
check and recommend; a human repair technician is always the one who
actually completes a repair or returns a device. Grounded directly in
this blueprint's own README text ("No automated advice can dispatch a
robot action the governor refuses, suppress an operating record, or
disclose sensitive data without governor approval and audit
evidence") -- a genuine DUAL-actuation shape (two distinct real-world
acts on the same ticket), structurally identical to `repairshop`/
9521's, `commrepair`/9512's, `applianceshop`/9522's, `furniture`/
9524's, `specialtyrepair`/9529's and `leathergoods`/9523's own
`:actuation/complete-repair`/`:actuation/return-*` shape (the same
repair-shop business archetype, applied to ICT equipment rather than
consumer electronics, communication equipment, household appliances,
furniture, watches/jewelry/bicycles or footwear/leather goods).

## The core contract

```
item intake + jurisdiction facts (ictrepair.facts, spec-cited)
        |
        v
   ┌───────────────────────┐   proposal      ┌───────────────────────┐
   │ RepairOps-LLM         │ ─────────────▶ │ Repair Governor              │  (independent system)
   │ (sealed)              │  + citations    │ spec-basis · evidence-       │
   └───────────────────────┘                 │ incomplete · parts-cost-     │
          │                 commit ◀┼ mismatch (honest reuse) ·        │
          │                         │ safety-test-not-passed (honest        │
    record + ledger        escalate ┼ reuse) · media-sanitization-              │
          │              (ALWAYS for│ unconfirmed (conditional, NEW) ·          │
          │       :actuation/complete│ already-completed · already-returned       │
          │       -repair/:actuation/│                                            │
          │       return-device)     │                                            │
          ▼                          └───────────────────────┘
      human approval
```

**The RepairOps-LLM never completes a repair or returns a device the
Repair Governor would reject, and never does so without a human sign-
off.** Hard violations (fabricated regulatory requirements;
unsupported evidence; a parts-cost mismatch; a failed safety test; an
unconfirmed media-sanitization status on a replaced storage
component; a double completion/return) force **hold** and *cannot* be
approved past; a clean completion/return proposal still always routes
to a human.

## Run

```bash
clojure -M:dev:run     # walk one clean dual-actuation lifecycle + five HARD-hold cases through the actor
clojure -M:dev:test    # governor contract · phase invariants · store parity · registry conformance · facts coverage
clojure -M:lint        # clj-kondo (errors fail; CI mirrors this)
```

## Robotics premise

All cloud-itonami verticals are designed on the premise that a **robot
performs the physical domain work**. Here a repair robot performs
disassembly, component handling, soldering and testing on devices,
under the actor, gated by the independent **Repair Governor**. The
governor never dispatches hardware itself; `:high`/`:safety-critical`
actions (handling customer devices, batteries and small components)
require human sign-off.

## Open business

This repository is not only source code. It is a public, forkable
business model:

| Layer | What is open |
|---|---|
| OSS core | Actor runtime, Repair Governor, repair-completion/device-return draft records, audit ledger |
| Business blueprint | Customer, offer, pricing, unit economics, sales motion |
| Operator playbook | How to fork, license, deploy and support the service in a jurisdiction |
| Trust controls | Governance, security reporting, actuation invariant, audit requirements |

See [`docs/business-model.md`](docs/business-model.md) and
[`docs/operator-guide.md`](docs/operator-guide.md) to start this as an
open business on itonami.cloud, and
[`docs/adr/0001-architecture.md`](docs/adr/0001-architecture.md) for the
full architecture and decision record.

## Capability layer

This blueprint resolves its technology stack via
[`kotoba-lang/industry`](https://github.com/kotoba-lang/industry) (ISIC
`9511`). This vertical's service/member records are practice-specific
rather than a shared cross-operator data contract, so `ictrepair.*`
runs on the generic robotics/identity/forms/dmn/bpmn/audit-ledger
stack only -- no bespoke domain capability lib to reference at all.

## Layout

| File | Role |
|---|---|
| `src/ictrepair/store.cljc` | **Store** protocol -- `MemStore` ‖ `DatomicStore` (`langchain.db`) + append-only audit ledger + repair-completion AND device-return history (dual history, mirroring `repairshop`/9521's, `commrepair`/9512's, `applianceshop`/9522's, `furniture`/9524's, `specialtyrepair`/9529's and `leathergoods`/9523's own shape). The double-actuation guard checks dedicated `:repair-completed?`/`:device-returned?` booleans rather than a `:status` value |
| `src/ictrepair/registry.cljc` | Repair-completion/device-return draft records, plus `parts-cost-matches-claim?` -- an HONEST, literal reuse of `repairshop.registry`'s/`commrepair.registry`'s/`applianceshop.registry`'s/`furniture.registry`'s/`specialtyrepair.registry`'s/`leathergoods.registry`'s own EXACT-MATCH independent-recompute check for the SAME real-world concern, not claimed as new |
| `src/ictrepair/facts.cljc` | Per-jurisdiction consumer-product-safety AND media-sanitization/data-destruction catalog (a genuine extension beyond every prior sibling's own catalog) with an official spec-basis citation per entry, honest coverage reporting -- ALL FOUR seeded jurisdictions have a media-sanitization sub-citation here |
| `src/ictrepair/repairopsllm.cljc` | **RepairOps-LLM** -- `mock-advisor` ‖ `llm-advisor`; intake/jurisdiction-assessment/safety-screening/media-screening/repair-completion/device-return proposals |
| `src/ictrepair/governor.cljc` | **Repair Governor** -- 7 HARD checks (spec-basis · evidence-incomplete · parts-cost-mismatch, honest reuse · safety-test-not-passed, honest reuse · media-sanitization-unconfirmed, CONDITIONAL unconditional evaluation, GENUINELY NEW, the 70th grounding of this discipline · already-completed guard · already-returned guard) + 1 soft (confidence/actuation gate) |
| `src/ictrepair/phase.cljc` | **Phase 0→3** -- read-only → assisted intake → assisted assess → supervised (repair completion/device return always human; ticket intake is the ONLY auto-eligible op, no direct capital risk) |
| `src/ictrepair/operation.cljc` | **OperationActor** -- langgraph StateGraph |
| `src/ictrepair/sim.cljc` | demo driver |
| `test/ictrepair/*_test.clj` | governor contract · phase invariants · store parity · registry conformance · facts coverage |

## Business-process coverage (honest)

This actor covers item intake through consumer-product-safety/media-
sanitization regulatory assessment, post-repair safety screening,
media-sanitization screening, repair completion and device return --
the core governed lifecycle this blueprint's own `docs/business-
model.md` names as its Offer:

| Covered | Not covered (out of scope for this R0) |
|---|---|
| Ticket intake + per-jurisdiction evidence checklisting, HARD-gated on an official spec-basis citation (`:ticket/intake`/`:jurisdiction/assess`) | Real repair-shop-management-system integration, real diagnostic/repair work itself (see `ictrepair.facts`'s docstring) |
| Post-repair safety screening + media-sanitization screening, each evaluated so the screening op itself can HARD-hold on its own finding (`:safety/screen`/`:media/screen`, the latter CONDITIONAL on the ticket's own storage-replacement ground truth) | Repair-quality judgment itself -- deliberately outside this actor's competence |
| Repair completion, HARD-gated on full evidence and a matching parts-cost claim, plus a double-completion guard (`:actuation/complete-repair`) | |
| Device return, HARD-gated on full evidence, a passed safety test and a confirmed media-sanitization status (when applicable), plus a double-return guard (`:actuation/return-device`) | |
| Immutable audit ledger for every intake/assessment/screening/completion/return decision | |

Extending coverage is additive: add the next gate (e.g. a warranty-
coverage-verification check) as its own governed op with its own HARD
checks and tests, following the SAME "an independent governor
re-verifies against the actor's own records before any real-world
act" pattern this repo's flagship ops already establish.

## Jurisdiction coverage (honest)

`ictrepair.facts/coverage` reports how many requested jurisdictions
actually have an official spec-basis in `ictrepair.facts/catalog` --
currently 4 seeded (JPN, USA, GBR, DEU) out of ~194 jurisdictions
worldwide. This is a starting catalog to prove the governor contract
end-to-end, not a claim of global coverage. Adding a jurisdiction is
additive: one map entry in `ictrepair.facts/catalog`, citing a real
official source -- never fabricate a jurisdiction's requirements to
make coverage look bigger. Note that the media-sanitization sub-
citation is FULL coverage rather than a gap: ALL FOUR seeded
jurisdictions (JPN, USA, GBR, DEU) actually have a real media-
sanitization/data-destruction enforcement regime, reported honestly --
the same honesty discipline that forbids fabricating coverage also
forbids under-reporting it.

## Maturity

`:implemented` -- `RepairOps-LLM` + `Repair Governor` run as real,
tested code (see `Run` above), promoted from the originally-published
`:blueprint`-tier scaffold, modeled closely on `repairshop`/9521's,
`commrepair`/9512's, `applianceshop`/9522's, `furniture`/9524's,
`specialtyrepair`/9529's and `leathergoods`/9523's own architecture
and the other prior actors' architecture across this fleet, with its
own distinct, independently-named governor. See
`docs/adr/0001-architecture.md` for the history and design.

## License

Code and implementation templates are AGPL-3.0-or-later.
