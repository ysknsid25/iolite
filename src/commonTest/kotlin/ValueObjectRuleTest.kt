import com.lemonappdev.konsist.api.KoModifier
import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.modifierprovider.withValueModifier
import com.lemonappdev.konsist.api.ext.list.primaryConstructors
import com.lemonappdev.konsist.api.ext.list.withInterfaceNamed
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.jupiter.api.Test

class ValueObjectRuleTest {
    @Test
    fun `companion object is last declaration in the value class`() {
        Konsist
            .scopeFromProject()
            .classes()
            .assertTrue {
                val companionObject = it.objects(includeNested = false).lastOrNull { obj ->
                    obj.hasModifier(KoModifier.COMPANION)
                }

                if (companionObject != null) {
                    it.declarations(includeNested = false, includeLocal = false).last() == companionObject
                } else {
                    true
                }
            }
    }

    @Test
    fun `every value class has parameter named 'value'`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withValueModifier()
            .primaryConstructors
            .assertTrue { it.hasParameterWithName("value") }
    }

    @Test
    fun `every value class implements ValueObject interface`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withValueModifier()
            .withInterfaceNamed("ValueObject")
            .assertTrue {
                it.hasInterfaces()
            }
    }
}
