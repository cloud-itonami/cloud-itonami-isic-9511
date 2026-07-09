# Business Model: Community ICT Equipment Repair

## Classification
- Repository: `cloud-itonami-9511`
- ISIC Rev.5: `9511` — ICT equipment repair — intake, diagnosis, quote, repair and return
- Social impact: circular-economy digital-inclusion worker-safety

## Customer
- community repair shops, refurbishers and right-to-repair programs leaving closed ticketing SaaS

## Offer
- intake and diagnosis, quote and approval, repair workflows, parts and warranty records, audit

## Revenue
- setup fee per shop, monthly operations subscription, parts and warranty services

## Trust Controls
- repairs outside approval are blocked; parts and warranty are auditable; customer device data stays outside Git
- a robot action the governor refuses is never dispatched to hardware
- every dispatch, hold, approval and disclosure path is auditable
- sensitive operating and personal data stays outside Git
- an unconfirmed media-sanitization status on a replaced/removed storage component (hard drive, SSD) forces a hold, un-overridable, before completion or return

## Repair Governor: a fresh, independently-named build

`cloud-itonami-isic-9511`'s `:itonami.blueprint/governor` keyword,
`:repair-governor`, is a UNIQUE keyword fleet-wide (grep-verified: no
other blueprint declares it) -- unlike the `:repair-shop-governor`
family (`repairshop`/9521, `commrepair`/9512, `applianceshop`/9522,
`furniture`/9524, `specialtyrepair`/9529, `leathergoods`/9523), there
is no governor-name-reuse question here. The genuinely new HARD check
this vertical adds is media-sanitization verification: ICT equipment
repair routinely replaces failed data-bearing storage components
(hard drives, SSDs), and the old component -- which may still hold
fully recoverable customer personal data -- must be securely
sanitized or destroyed before disposal. This is grounded in NIST SP
800-88 Rev.1 (US), the UK GDPR, Germany's DSGVO/BDSG, and Japan's own
個人情報保護法 (APPI). It is structurally DISTINCT from `commrepair`/
9512's own customer-data-consent check (consent to access data DURING
repair) -- this check is about secure DESTRUCTION of a REMOVED
storage component AFTER the repair decision to replace it.
