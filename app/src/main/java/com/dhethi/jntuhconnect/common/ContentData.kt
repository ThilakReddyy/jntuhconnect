package com.dhethi.jntuhconnect.common

/**
 * Curated static content that mirrors the web frontend's content pages (channels,
 * calendars, syllabus, careers, help) plus the shared filter option lists. Links point
 * to the canonical web resources so the app stays aligned with the site.
 */
object ContentData {

    const val WEB_BASE = "https://jntuhconnect.dhethi.com"
    const val IMP_QUESTIONS_URL = "https://jntuh-iq.vercel.app/"

    // ---- Notification / syllabus filters (from the frontend constants) ----
    val degrees = listOf("B.Tech", "B.Pharmacy", "M.Tech", "M.Pharmacy", "MBA", "MCA")
    val examYears = listOf("2026", "2025", "2024", "2023", "2022", "2021", "2020", "2019", "2018")
    val regulations = listOf(
        "R25" to "2025-2029",
        "R24" to "2024-2028",
        "R23" to "2023-2027",
        "R22" to "2022-2026",
        "R21" to "2021-2025",
        "R20" to "2020-2024",
        "R19" to "2019-2023",
        "R18" to "2018-2022"
    )

    // ---- Channels ----
    data class ChannelLink(
        val name: String,
        val description: String,
        val url: String,
        val tag: String
    )

    val channels = listOf(
        ChannelLink(
            name = "Telegram · JNTUH Connect",
            description = "Instant alerts the moment JNTUH exam results are published.",
            url = "https://t.me/jntuhvercel",
            tag = "Telegram"
        ),
        ChannelLink(
            name = "WhatsApp Channel",
            description = "Quick result alerts and important university updates.",
            url = "https://chat.whatsapp.com/EBIhYt8Jt9rJFNrgUsbmiR",
            tag = "WhatsApp"
        ),
        ChannelLink(
            name = "@__thilak_reddy__",
            description = "Follow the creator on Instagram for updates and tech content.",
            url = "https://www.instagram.com/__thilak_reddy__/",
            tag = "Instagram"
        )
    )

    // ---- Social links (Profile) ----
    data class SocialLink(val name: String, val url: String)

    val socials = listOf(
        SocialLink("GitHub", "https://github.com/thilakreddyy"),
        SocialLink("Twitter / X", "https://twitter.com/thilakreddyonly"),
        SocialLink("Instagram", "https://www.instagram.com/__thilak_reddy__/")
    )

    // Academic calendars and syllabus are now fetched live from the backend
    // (GET /api/calendars, GET /api/syllabus) — see the content drill-down screens.

    // ---- Careers / opportunities ----
    data class CareerItem(
        val title: String,
        val company: String,
        val location: String,
        val tags: List<String>,
        val about: String,
        val applyUrl: String
    )

    val careers = listOf(
        CareerItem(
            title = "Software Engineer — New Grad",
            company = "Top Product Companies",
            location = "Bengaluru / Hyderabad, India",
            tags = listOf("Full-time", "0-1 yrs", "Engineering"),
            about = "Curated new-grad software roles for JNTUH students. Explore the full, always-updated list on the web.",
            applyUrl = "$WEB_BASE/carrers"
        )
    )

    // ---- FAQ ----
    data class FaqItem(val question: String, val answer: String)

    val faqs = listOf(
        FaqItem(
            "How do I check my result?",
            "Enter your 10-character JNTUH roll number on the Home screen and tap search. You'll see your consolidated academic result, all attempts, backlogs and credits."
        ),
        FaqItem(
            "Why does it say my roll number is 'queued'?",
            "If your result isn't cached yet, we ask the server to fetch it in the background. Wait a few moments and try again."
        ),
        FaqItem(
            "How is CGPA calculated?",
            "For each subject the best grade across all attempts (regular, supplementary, RCRV, grace) is used. CGPA is credit-weighted and shows only when you have no active backlogs."
        ),
        FaqItem(
            "What is the Credits Checker?",
            "It compares the credits you've earned against your regulation's required-credits table, year by year. It's currently available for B.Tech students."
        ),
        FaqItem(
            "Is Grace Marks applicable to me?",
            "Grace marks apply to final-year B.Tech / B.Pharmacy students with pending backlogs, once 4-2 results are synced. Use the Grace Marks tool to check eligibility."
        ),
        FaqItem(
            "The result differs from the JNTUH site?",
            "We aggregate the best attempt per subject. Use the 'Direct Link' on the All Results tab to verify against the official JNTUH page."
        )
    )
}
