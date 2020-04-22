package gui.config.formcomponents

interface FormInput : FormComponent {
    fun validate(): List<String>
    fun save()
    fun value(): Any
}