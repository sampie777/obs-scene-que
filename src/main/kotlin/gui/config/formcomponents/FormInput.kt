package gui.config.formcomponents

interface FormInput : FormComponent {
    val key: String
    fun validate(): List<String>
    fun save()
    fun value(): Any
}