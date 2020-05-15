package plugins.easyworship.queItems

class EasyWorshipNextVerseQueItem : EasyWorshipQueItem("Next verse") {
    override fun activate() {
        println("Activate next verse")
    }

    override fun deactivate() {}
}