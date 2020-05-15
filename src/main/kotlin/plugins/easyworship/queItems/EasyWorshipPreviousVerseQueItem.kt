package plugins.easyworship.queItems

class EasyWorshipPreviousVerseQueItem : EasyWorshipQueItem("Previous verse") {
    override fun activate() {
        println("Activate previous verse")
    }

    override fun deactivate() {}
}