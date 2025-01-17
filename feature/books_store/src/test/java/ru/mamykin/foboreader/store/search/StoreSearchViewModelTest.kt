package ru.mamykin.foboreader.store.search

import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import ru.mamykin.foboreader.core.presentation.StringOrResource
import java.io.IOException
import java.io.StringReader



class StoreSearchViewModelTest {

    private val searchInStoreUseCase: SearchInStoreUseCase = mockk()
    private val viewModel = StoreSearchViewModel(searchInStoreUseCase)
    private val searchSuccessfulResult = SearchResultsEntity(
        emptyList(),
        emptyList(),
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `initial state is search not started`() {
        val state = viewModel.state

        assertTrue(state.searchQuery.isEmpty())
        assertEquals(StoreSearchViewModel.SearchState.NotStarted, state.searchState)
    }

    @Test
    fun `search shows results when succeeds`() = runTest {
        val searchQuery = "a"
        coEvery { searchInStoreUseCase.execute(searchQuery) } returns Result.success(searchSuccessfulResult)

        viewModel.sendIntent(StoreSearchViewModel.Intent.Search(searchQuery))
        advanceUntilIdle()

        val state = viewModel.state
        assertEquals(searchQuery, state.searchQuery)
        assertEquals(
            StoreSearchViewModel.SearchState.Loaded(
                searchSuccessfulResult.categories,
                searchSuccessfulResult.books,
            ), state.searchState
        )
    }

    @Test
    fun `search shows error when fails`() = runTest {
        val errorMessage = "test"
        val searchQuery = "a"
        coEvery { searchInStoreUseCase.execute(any()) } returns Result.failure(RuntimeException(errorMessage))

        viewModel.sendIntent(StoreSearchViewModel.Intent.Search(searchQuery))
        advanceUntilIdle()

        val state = viewModel.state
        assertEquals(searchQuery, state.searchQuery)
        assertEquals(
            StoreSearchViewModel.SearchState.Failed(StringOrResource.String(errorMessage)), state.searchState
        )
    }

    @Test
    fun `search cancels the previous active search`() = runTest {
        // val searchQuery = "a"
        // coEvery { searchInStoreUseCase.execute(any()) } returns Result.success(searchSuccessfulResult)
        //
        // viewModel.sendIntent(StoreSearchViewModel.Intent.Search(searchQuery))
        // viewModel.sendIntent(StoreSearchViewModel.Intent.Search(searchQuery))
        // advanceUntilIdle()
        //
        // val state = viewModel.state
        // assertEquals(searchQuery, state.searchQuery)
        // assertEquals(
        //     StoreSearchViewModel.SearchState.Failed(StringOrResource.String(errorMessage)), state.searchState
        // )
    }

    private val sourceStr = """
        <p>A long time ago a countryman had a son who was as big as a thumb, and did not become any bigger, and during several years did not grow one hair’s breadth. Once when the father was going out to plough, the little one said: </p>
        <t>Давным-давно жил один крестьянин, У которого был маленький-премаленький сынишка – не больше мизинца. Вот как-то раз собрался крестьянин в поле пахать, а мальчик с пальчик и говорит: </t>

        <p>- Father, I will go out with thee! </p>
        <t>–Возьми меня с собой! </t>

        <p>- Thou wouldst go out with me? - said the father. - Stay here, thou wilt be of no use out there, besides thou mightst get lost. </p>
        <t>–Куда тебе! Ведь ты ещё не можешь пахать,– отвечал ему отец.– Сиди-ка лучше дома, а то, чего доброго, потеряешься в поле. </t>

        <p>Then Thumbling began to cry, and for the sake of peace his father put him in his pocket, and took him with him. When he was outside in the field, he took him out again, and set him in a freshly-cut furrow.</p>
        <t>Но мальчик с пальчик горько заплакал. Чтобы успокоить сына, отец сунул его в карман и пошёл в поле. Там он посадил мальчика в свежую борозду и стал пахать. </t>

        <p>While he was there, a great giant came over the hill. </p>
        <t>Сидит мальчик с пальчик в борозде и вдруг видит: из-за дальней горы показался великан. </t>

        <p>- Dost thou see that great monster?” said the father, for he wanted to frighten the little fellow to make him good.</p>
        <t>–Ага, вот он сейчас тебя утащит!– припугнул мальчугана отец. </t>

        <p>The giant, however, had scarcely taken two steps with his long legs before he was in the furrow. He took up little Thumbling carefully with two fingers, examined him, and without saying one word went away with him.</p>
        <t>Только он успел это сказать, а великан уже тут как тут – шагнул два раза своими длинными ногами и оказался у самой борозды. Осторожно, двумя пальцами, поднял великан мальчика с пальчик с земли, оглядел его молча со всех сторон и зашагал с мальчиком на руке в горы. </t>

        <p>His father stood by, but could not utter a sound for terror, and he thought nothing else but that his child was lost, and that as long as he lived he should never set eyes on him again.</p>
        <t>А отец стоял, онемев от страха и горя, и думал, что уж никогда больше не увидит своего сынишку. </t>

        <p>The giant, however, carried him home, suckled him, and Thumbling grew and became tall and strong after the manner of giants.</p>
        <t>Принес великан мальчика с пальчик к себе в дом и стал кормить его такой пищей, от которой люди превращаются в великанов. Мальчик с пальчик быстро рос и становился всё сильнее и сильнее. </t>

        <p>When two years had passed, the old giant took him into the forest, wanted to try him, and said:</p>
        <t>Прожил так мальчик у великана два года. Тогда привёл его великан в лес и говорит: </t>

        <p>- Pull up a stick for thyself.</p>
        <t>–А ну-ка, выдерни себе палочку! </t>

        <p>Then the boy was already so strong that he tore up a young tree out of the earth by the roots. </p>
        <t>Мальчик ухватился за молоденькое деревцо да и вытащил его вместе с корнем. Вот какой он стал сильный! </t>

        <p>But the giant thought, “We must do better than that,”</p>
        <t>–Нет, ты ещё не очень силён,– сказал великан. </t>

        <p>took him back again, and suckled him two years longer. When he tried him, his strength had increased so much that he could tear an old tree out of the ground.</p>
        <t>Он увёл мальчика домой и кормил ещё два года. А через два года мальчик стал такой сильный, что выдернул из земли большое, старое дерево. </t>

        <p>That was still not enough for the giant; he again suckled him for two years, and when he then went with him into the forest and said</p>
        <t>–Ну нет, ты ещё не совсем сильный!– сказал великан и опять увел его домой. А еще через два года снова пошел великан с мальчиком в лес и сказал: </t>

        <p>-Now, just tear up a proper stick for me</p>
        <t>–Ну-ка, выдерни себе палочку! </t>

        <p>The boy tore up the strongest oak tree from the earth.</p>
        <t>Мальчик взял да и выдернул из земли прямо с корнями здоровенный, старый дуб. </t>

        <p>“Now that will do, - said the old giant</p>
        <t>–Вот теперь ты стал силачом!– сказал старый великан. </t>

        <p>And took him back to the field from whence he had brought him.</p>
        <t>И он отвёл мальчика обратно на то поле, где когда-то взял его. </t>

        <p>His father was there following the plough. The young giant went up to him, and said:</p>
        <t>Крестьянин и на этот раз пахал. Молодой великан подошел к нему и сказал: </t>

        <p>- Does my father see what a fine man his son has grown into</p>
        <t>–Погляди-ка, отец, каким я стал большим да сильным! </t>

        <p>The farmer was alarmed by young giant.</p>
        <t>Но крестьянин испугался молодого великана. </t>

        <p>- No, thou art not my son; I don’t want thee- leave me!</p>
        <t>–Нет, нет, какой я тебе отец! Уходи отсюда!– отвечал он. </t>

        <p>- Truly I am your son; allow me to do your work, I can plough as well as you, nay better.</p>
        <t>–Да правда же, отец, я твой сын! Посмотри, как хорошо я умею пахать! Даже лучше, чем ты. </t>

        <p>- No, no, thou art not my son, and thou canst not ploughgo away!</p>
        <t>–Нет, нет, совсем ты мне не сын! И пахать ты не умеешь. Уходи!– твердил крестьянин. </t>

        <p>However, as he was afraid of this great man</p>
        <t>Он боялся, как бы огромный человек не сделал ему чего-нибудь плохого. </t>

        <p>Then the youth took the plough, and just pressed it with one hand, but his grasp was so strong that the plough went deep into the earth.</p>
        <t>А великан подошёл к плугу и только взялся за него одной рукой – он так и врезался до половины в землю. </t>

        <p>The farmer could not bear to see that, and called to him:</p>
        <t>Не выдержал тут крестьянин и крикнул: </t>

        <p>- If thou art determined to plough, thou must not press so hard on it, that makes bad work.</p>
        <t>–Ну нет, такая работа никуда не годится! Хочешь пахать, так паши как следует, а не нажимай что есть мочи. </t>

        <p>The youth, however, unharnessed the horses, and drew the plough himself, saying:</p>
        <t>Тогда великан выпряг из плуга лошадей и впрягся вместо них сам. А отцу сказал: </t>

        <p>- Just go home, father, and bid my mother make ready a large dish of food, and in the meantime I will go over the field</p>
        <t>–Иди-ка, отец, домой да скажи матери, чтобы побольше еды приготовила, а я за это время поле вспашу. </t>

        <p>Then the farmer went home, but the youth ploughed the field, uite alone,</p>
        <t>Отец пошёл домой, а молодой великан вспахал и забороновал один всё большое поле. </t>

        <p>When he had done it, he went into the forest, and pulled up two oak trees, laid them across his shoulders, and hung one harrow on them behind and one before, and also one horse behind and one before, and carried all as if it had been a bundle of straw, to his parents’ house. When he entered the yard, his mother did not recognize him, and asked:</p>
        <t>Потом пошёл в лес и вырвал там с корнем два дуба. Взвалил великан оба дерева себе на плечи, повесил на концах дубов, спереди и сзади, по бороне да по лошади и понёс домой. И нёс все так легко, как охапку соломы. Увидала его мать из окна и спросила мужа: </t>

        <p>- Who is that horrible tall man?</p>
        <t>–Что это там за громадина идёт? </t>

        <p>- That is our son, - the farmer said</p>
        <t>–Это наш сын,– отвечал крестьянин. </t>

        <p>- No, that cannot be our son, we never had such a tall one, ours was a little thing.</p>
        <t>–Что ты! Он ничуть не похож на нашего сына,– сказала жена.– Наш-то был совсем малюсенький, а этот вон какой великан! </t>

        <p>She called to him:</p>
        <t>Тут их сын подошёл к дому, и она закричала: </t>

        <p>- Go away, we do not want thee!</p>
        <t>–Уходи, уходи подобру-поздорову! Ты нам совсем не нужен. </t>

        <p>The youth was silent, but led his horses to the stable, gave them oats and hay, and all that they wanted. When he had done this, he went into the parlor, sat down on the bench and said:</p>
        <t>Великан ничего не ответил. Он отвел лошадей в конюшню, засыпал им овса – всё как следует, потом вошёл в дом, сел на лавку и сказал: </t>

        <p>- Mother, now I should like something to eat, will it soon be ready?</p>
        <t>–А ну-ка, мать, давай скорее обедать, уж больно мне есть хочется! </t>

        <p>Brought in two immense dishes full of food, which would have been enough to satisfy herself and her husband for a week.</p>
        <t>Мать поставила перед ним на стол две большущие миски каши, полные до краёв, и подумала: «Нам бы с мужем этой еды на целую неделю хватило». </t>

        <p>The youth, however, ate the whole of it himself, and asked</p>
        <t>А великан съел быстрёхонько всю кашу и говорит: </t>

        <p>- Do you have anything more for me?</p>
        <t>–Нет ли там ещё чего-нибудь? </t>

        <p>“No,” she replied, “that is all we have.” “But that was only a taste,</p>
        <t>–Нет,– отвечала мать.– Мы отдали тебе всё, что у нас было. </t>

        <p>- I must have more. Father, I see well that with thee I shall never have food enough; if thou will get me an iron staff which is strong, and which I cannot break against my knees, I will go out into the world</p>
        <t>–А я совсем не наелся и хочу ещё,– сказал великан.– Вижу я, трудно вам будет меня досыта кормить. Пойду-ка я лучше бродить по свету. </t>

        <p>So he went away.</p>
        <t>И отправился в путь. </t>

        <p>He arrived at a village, wherein lived a smith who was a greedy fellow, who never did a kindness to any one, but wanted everything for himself. The youth went into the smithy to him, and asked if he needed a journeyman.</p>
        <t>Вот пришёл он в деревню, где жил кузнец, очень жадный и завистливый. Великан спросил кузнеца, не нужен ли ему подмастерье. </t>

        <p>smith looked at him, and thought, “That is a strong fellow who will strike out well, and earn his bread.”</p>
        <t>Взглянул кузнец на великана и подумал: «Ну, этот здоровяк сумеет работать, не зря будет деньги получать». </t>

        <p>So he asked, “How much wages dost thou want”</p>
        <t>–А сколько тебе надо платить за работу?– спросил он великана. </t>

        <p>“I don’t want any at all,” he replied, “only every fortnight, when the other journeymen are paid, I will give thee two blows, and thou must bear them.”</p>
        <t>–Я буду работать совсем даром,– отвечал великан.– Но только каждый раз, когда другие работники получают деньги, я хочу давать тебе два пинка. </t>

        <p>The miser was heartily satisfied, and thought he would thus save much money. Next morning, the strange journeyman was to begin to work, but when the master brought the glowing bar, and the youth struck his first blow, the iron flew asunder, and the anvil sank so deep into the earth, that there was no bringing it out again.</p>
        <t>Скряга кузнец очень обрадовался, что его денежки останутся при нём, и согласился. На другой день захотел кузнец поглядеть, как работает его новый подмастерье. Принёс он толстенную полосу раскалённого железа и положил её на наковальню. Великан как ударил молотом – полоса тут же и разлетелась на две части. А наковальня так глубоко в землю врезалась, что её и вытащить не смогли. </t>

        <p>Then the miser grew angry, and said:</p>
        <t>Разозлился скряга кузнец и закричал: </t>

        <p>“Oh, but I can’t make any use of thee, thou strikest far too powerfully; what wilt thou have for the one blow?”</p>
        <t>–Не нужен мне такой работник! У тебя слишком тяжёлая рука – ишь как колотишь! Сколько тебе заплатить за этот один удар? </t>

        <p>Then said he, “I will only give thee quite a small blow, that’s all.”</p>
        <t>–Я дам тебе за него только один, и совсем слабенький, пиночек,– отвечал великан. </t>

        <p>And he raised his foot, and gave him such a kick that he flew away over four loads of hay.</p>
        <t>И он так наподдал кузнеца, что тот через пять стогов сена перелетел. </t>

        <p>Then he sought out the thickest iron bar in the smithy for himself, took it as a stick in his hand, and went onwards.</p>
        <t>А великан сковал себе в кузнице огромную железную палку и пошёл по белу свету бродить. </t>

        <p>When he had walked for some time, he came to a small farm</p>
        <t>Шёл он, шёл и пришёл в деревню, где жил один жадный богач. </t>

        <p>and asked the bailiff if he did not require a head-servant.</p>
        <t>–Не нужен ли вам работник?– спросил он богача. </t>

        <p>“Yes,” said the bailiff, “I can make use of one; you look a strong fellow who can do something, how much a year do you want as wages?”</p>
        <t>–Да,– отвечал тот,– мне нужны такие здоровые парни, как ты. А много ль ты хочешь получать за свою работу? </t>

        <p>He again replied that he wanted no wages at all, but that every year he would give him three blows, which he must bear.</p>
        <t>–Я буду работать совсем даром, если вы согласны каждый год получать от меня по три пинка. </t>

        <p>Then the bailiff was satisfied, for he, too, was a covetous fellow.</p>
        <t>Богач согласился. Он был таким же скрягой, как и кузнец, и очень обрадовался, что нашел бесплатного работника. </t>

        <p>The bailiff, however, was afraid of the blows which he was to receive, and earnestly entreated him to excuse him from having them;</p>
        <t>Вот прослужил великан у этого богача год и стал требовать расплаты. Но хозяин испугался и принялся упрашивать великана пожалеть его. </t>

        <p>The bailiff was willing to give him whatsoever he demanded, but it was of no use, the head-servant said no to everything.</p>
        <t>–Возьми сколько хочешь денег, возьми все мое добро и будь сам хозяином, только не трогай меня!– взмолился он. </t>

        <p>“No,” said he, “I will not be a bailiff, I am headservant, and will remain so, but I will administer that which we agreed on.</p>
        <t>–Нет,– отвечал великан,– я как был работником, так и хочу им быть. А то, что мне причитается, отдавай! </t>

        <p>Then the bailiff did not know what to do, and begged for a fortnight’s delay, for he wanted to find some way to escape.</p>
        <t>Понял тогда богач, что от великана не так-то легко отделаться, и попросил его подождать две недели. </t>

        <p>The head-servant consented to this delay. The bailiff summoned all his clerks together, and they were to think the matter over, and give him advice.</p>
        <t>Великан согласился. А хозяин собрал всех своих родных и попросил их посоветовать, как ему быть. </t>

        <p>The clerks pondered for a long time, but at last they said</p>
        <t>Долго думали они и наконец решили так: </t>

        <p>He could kill a man as easily as a midge, and that the bailiff ought to make him get into the well and clean it, and when he was down below, they would roll up one of the mill-stones which was lying there, and throw it on his head; and then he would never return to daylight.</p>
        <t>–Пошли-ка ты своего работника чистить колодец. И когда он будет в колодце, сбрось ему на голову мельничный жёрнов. Вот и избавимся мы от него. А то ведь этому громадине стоит только захотеть – он всех нас, как комаров, может передушить. </t>

        <p>The advice pleased the bailiff, and the head-servant was quite willing to go down the well.</p>
        <t>Богачу понравился этот совет, и он послал великана чистить колодец. Великан пошёл. </t>

        <p>When he was standing down below at the bottom, they rolled down the largest mill-stone and thought they had broken his skull</p>
        <t>И вот, когда он стоял на дне колодца, несколько человек подкатили к колодцу тяжёлый жернов и сбросили его на великана. «Ну теперь-то уж мы избавились от него!» – подумал хозяин. </t>

        <p>Suddenly, they heard a voice from the well:</p>
        <t>И вдруг они услыхали из колодца голос. </t>

        <p>Chase away those hens from the well, they are scratching in the sand up there, and throwing the grains into my eyes, so that I can’t see.</p>
        <t>–Эй вы там!– кричал великан.– Отгоните-ка кур от колодца, а то они копаются в песке, и песок сыплется прямо мне на голову. Совсем глаза засорил! </t>

        <p>So the bailiff cried, “Shsh”- and pretended to frighten the hens away.</p>
        <t>–Кш, кш, пошли отсюда!– закричал хозяин, чтобы великан думал, будто он отгоняет кур. </t>

        <p>When the head-servant had finished his work, he climbed up and said:</p>
        <t>Великан вычистил колодец, вылез из него и сказал: </t>

        <p>Just look what a beautiful necktie I have on,</p>
        <t>–Смотрите, какое у меня ожерелье! </t>

        <p>and behold it was the mill-stone which he was wearing round his neck. </p>
        <t>И все увидели у него на шее жёрнов. </t>

        <p>The head-servant now wanted to take his reward, but the bailiff again begged for a fortnight’s delay.</p>
        <t>Стал великан опять требовать у хозяина расплаты. Но богач попросил его подождать еще две недели, и великан согласился. </t>

        <p>The clerks met together.</p>
        <t>Вот собрались снова все родственники богача. </t>

        <p>advised him to send the head-servant to the haunted mill to grind corn by night</p>
        <t>Думали, думали и решили послать великана ночью на заколдованную мельницу зерно молоть. </t>

        <p>from thence as yet no man had ever returned in the morning alive - they said</p>
        <t>–Уж тогда-то мы от него избавимся, потому что каждый, кто остаётся ночью на этой мельнице, погибает,– сказали они. </t>

        <p>The proposal pleased the bailiff, he called the headservant that very evening, and ordered him to take eight bushels of corn to the mill, and grind it that night, for it was wanted. So the head-servant went to the loft, and put two bushels in his right pocket, and two in his left, and took four in a wallet, half on his back, and half on his breast, and thus laden went to the haunted mill.</p>
        <t>Богач позвал великана, велел ему ехать на мельницу и смолоть там за ночь восемь пудов зерна. Насыпал великан в один карман два пуда зерна да в другой два пуда, а остальные четыре пуда перекинул в мешках через плечо и пошёл на заколдованную мельницу. </t>

        <p>The miller told him that he could grind there very well by day, but not by night, for the mill was haunted, and that up to the present time whosoever had gone into it at night had been found in the morning, lying dead inside</p>
        <t>–Разве ты не знаешь, что эта мельница заколдована? На ней можно работать только днём, а ночью здесь опасно, и ты можешь погибнуть,– сказал ему мельник. </t>

        <p>He said, “I will manage it, just you go away to bed.”</p>
        <t>–Ничего!– отвечал великан.– Спи спокойно, а я и сам о себе позабочусь. </t>

        <p>Then he went into the mill, and poured out the corn. About eleven o’clock he went into the miller’s room, and sat down on the bench. When he had sat there a while, a door suddenly opened, and a large table came in, and on the table, wine and roasted meats placed themselves, and much good food besides, but everything came of itself, for no one was there to carry it. After this the chairs pushed themselves up, but no people came, until all at once he beheld fingers, which handled knives and forks, and laid food on the plates, but with this exception he saw nothing.</p>
        <t>Высыпал он зерно, сел в комнате на лавку и стал ждать. А время-то было уже позднее: полночь наступила. Вот видит великан – что за чудо! Открылась дверь, и в комнату въехал большой-пребольшой стол. А на столе сами собой появились всякие вкусные кушанья и вино. Потом, откуда ни возьмись, показались стулья, хотя вокруг не было .видно ни одного человека. И наконец, замелькали вилки и ножи, как будто чьи-то руки накладывали на тарелку еду, наливали вино, подносили вино и еду ко рту. А самих людей не было видно. </t>

        <p>When he had had enough, and the others also had quite emptied their dishes, he distinctly heard all the candles being suddenly snuffed out, and as it was now pitch dark, he felt something like a box on the ear.</p>
        <t>Великану очень захотелось есть. А потому, не долго думая, он уселся за накрытый стол и угостился на славу. Когда он наелся досыта и у всех невидимых людей тарелки тоже стали пустыми, он увидел, как одна за другой начали гаснуть свечи. Они погасли все до единой, и стало совсем темно. И вдруг великану показалось, будто кто ударил его по лицу. </t>

        <p>Then he said:</p>
        <t>Но он ничуть не испугался и сказал: </t>

        <p>If anything of that kind comes again, I shall strike out in return</p>
        <t>–А ну-ка, попробуйте только тронуть ещё раз, так сдачи получите! </t>

        <p>And when he had received a second box on the ear, he, too, struck out. And so it continued the whole night, he took nothing without returning it, but repaid everything with interest, and did not lay about him in vain. At daybreak, however, everything ceased.</p>
        <t>Его ударили опять. Тогда он размахнулся и тоже ударил. Началась драка да так и продолжалась всю ночь. А когда стало рассветать, все невидимки исчезли. </t>

        <p>When the miller had got up, he wanted to look after him, and wondered if he were still alive.</p>
        <t>Пришёл мельник утром, смотрит – а великан-то жив-невредим! Ну и удивился же мельник! </t>

        <p>Then the youth say:</p>
        <t>А великан говорит: </t>

        <p>I have eaten my fill, have received some boxes on the ear, but I have given some in return.</p>
        <t>–Уж и наелся же я сегодня ночью! Да и подрался вволю. Ни одного удара не спустил врагам. </t>

        <p>The miller was very rejoiced.</p>
        <t>Мельник очень обрадовался. </t>

        <p>By your bravery mill was now released from the spell, - his sayed and wanted to give him much money as a reward.</p>
        <t>–Своей храбростью ты избавил мельницу от колдовства,– сказал он великану и хотел дать ему много денег. </t>

        <p>But giant said, “Money, I will not have, I have enough of it.”</p>
        <t>–Не надо мне денег,– отвечал великан.– Для чего они мне! </t>

        <p>So he took his meal on his back, went home, and told the bailiff that he had done what he had been told to do, and would now have the reward agreed on.</p>
        <t>Он взвалил на плечи мешки с мукой, пошел к хозяину и снова стал требовать у него обещанную плату. </t>

        <p>When the bailiff heard that, he was seriously alarmed and quite beside himself; he walked backwards and forwards in the room, and drops of perspiration ran down from his forehead.</p>
        <t>Понял тут богач, что не избавиться ему от великана. И так ему стало страшно, так страшно! </t>

        <p>Then he opened the window to get some fresh air, but before he was aware the head-servant had given him such a kick that he flew through the window out into the air, and so far away that no one ever saw him again.</p>
        <t>Заметался богач из угла в угол по комнате и подбежал к окну. Но только он открыл окно, великан дал ему хорошего пинка. Вылетел богач кувырком, и с тех пор его больше не видали. </t>

        <p>The young giant took up his iron bar, and went on his way.</p>
        <t>А юный великан взял свою палку и пошёл дальше по белу свету бродить.</t>
    """.trimIndent()

    @Test
    fun convertXmlToJson() {
        // val parser = XmlPullParserFactory.newInstance()
        parseXml()
    }

    fun parseXml() {
        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()

            xpp.setInput(StringReader(sourceStr))
            var eventType = xpp.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_DOCUMENT -> {
                        println("Start document")
                    }

                    XmlPullParser.START_TAG -> {
                        println("Start tag " + xpp.name)
                    }

                    XmlPullParser.END_TAG -> {
                        println("End tag " + xpp.name)
                    }

                    XmlPullParser.TEXT -> {
                        println("Text " + xpp.text)
                    }
                }
                eventType = xpp.next()
            }
            println("End document")
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}