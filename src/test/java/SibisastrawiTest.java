import com.transibi.Sibisastrawi;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;

import static org.hamcrest.CoreMatchers.instanceOf;

public class SibisastrawiTest extends TestCase {
    private static Sibisastrawi sibisastrawi;

    @Before
    @Override
    public void setUp() {
        String rootWords[] = {"nilai", "hancur", "benar", "apa", "siapa", "jubah",
                "baju", "celana", "hantu", "beli", "jual", "buku", "milik", "kulit",
                "beri", "sakit", "kasih", "buang", "sakit", "suap", "adu", "rambut",
                "suara", "daerah", "ajar", "kerja", "ternak", "asing", "raup", "gerak",
                "puruk", "terbang", "lipat", "ringkas", "warna", "yakin", "bangun",
                "fitnah", "vonis", "baru", "labuh", "minum", "pukul", "cinta", "dua",
                "jauh", "ziarah", "nuklir", "tangkap", "gila", "hajar", "qasar",
                "udara", "kupas", "suara", "populer", "warna", "yoga", "adil", "rumah",
                "muka", "tarung", "percaya", "serta", "pengaruh", "kritik",
                "sekolah", "tahan", "capai", "mulai", "tani", "abai",
                "syarat", "syukur", "bom", "promosi", "proteksi", "prediksi", "kaji",
                "sembunyi", "langgan", "laku", "baik", "bisik", "terang", "iman", "puas",
                "makan", "nyala", "nyanyi", "nyata", "nyawa", "rata", "lembut", "ligas",
                "kerja", "balas", "tebar", "daya", "makmur", "untung", "sepuluh", "ekonomi",
                "peran", "medan", "ideal", "final", "taat", "lewat", "nganga", "bagai",
                "badan", "tiru", "sepak", "kuasa", "nikmat", "malaikat"
        };

        sibisastrawi = new Sibisastrawi(rootWords);
    }

    public void testImplementsLemmatizerInterface() {
        Assert.assertThat(sibisastrawi, instanceOf(Sibisastrawi.class));
    }

    private void assertLemma(String word, String lemma) {
        assertEquals(lemma, Sibisastrawi.stem(word));
    }

    public void testDontStemWordFoundInDictionary() {
        assertLemma("nilai", "nilai");
    }

    public void testDontStemShortWords() {
        //assertLemma("mei", "mei");
        assertLemma("bui", "bui");
    }

    public void testLahKahTahPun() {
        assertLemma("hancurlah", "hancur lah");
        assertLemma("benarkah", "benar kah");
    }

    public void testKuMuNya() {
        //assertLemma("jubahku", "jubah aku");
        assertLemma("bajumu", "baju kamu");
        assertLemma("celananya", "celana nya");
    }

    public void testIKanAn() {
        assertLemma("hantui", "hantu i");
        assertLemma("belikan", "beli kan");
        assertLemma("jualan", "jual an");
    }

    public void testSuffixCombination() {
        assertLemma("bukumukah", "buku kamu kah");
        assertLemma("miliknyalah", "milik nya lah");
        assertLemma("kulitkupun", "kulit aku pun");
        assertLemma("berikanku", "beri kan aku");
        assertLemma("sakitimu", "sakit i kamu");
        assertLemma("beriannya", "beri an nya");
        assertLemma("kasihilah", "kasih i lah");
    }

    public void testPlainPrefix() {
        assertLemma("dibuang", "di buang");
        assertLemma("kesakitan", "ke sakit an");
        assertLemma("sesuap", "se suap");
    }

    public void testPrefixDisambiguation() {
        // rule 1a : berV -> ber-V
        assertLemma("beradu", "be adu");

        // rule 1b : berV -> be-rV
        assertLemma("berambut", "be rambut");

        // rule 2 : berCAP -> ber-CAP
        assertLemma("bersuara", "be suara");

        // rule 3 : berCAerV -> ber-CAerV where C != 'r'
        assertLemma("berdaerah", "be daerah");

        // rule 4 : belajar -> bel-ajar
        assertLemma("belajar", "be ajar");

        // rule 5 : beC1erC2 -> be-C1erC2 where C1 != {'r'|'l'}
        assertLemma("bekerja", "be kerja");
        assertLemma("beternak", "be ternak");

        // rule 6a : terV -> ter-V
        assertLemma("terasing", "te asing");

        // rule 6b : terV -> te-rV
        assertLemma("teraup", "te raup");

        // rule 7 : terCerV -> ter-CerV where C != 'r'
        assertLemma("tergerak", "te gerak");

        // rule 8 : terCP -> ter-CP where C != 'r' and P != 'er'
        assertLemma("terpuruk", "te puruk");

        // rule 9 : teC1erC2 -> te-C1erC2 where C1 != 'r'
        assertLemma("teterbang", "te terbang");

        // rule 10 : me{l|r|w|y}V -> me-{l|r|w|y}V
        assertLemma("melipat", "me lipat");
        assertLemma("meringkas", "me ringkas");
        assertLemma("mewarnai", "me warna i");
        assertLemma("meyakinkan", "me yakin kan");

        // rule 11 : mem{b|f|v} -> mem-{b|f|v}
        assertLemma("membangun", "me bangun");
        assertLemma("memfitnah", "me fitnah");
        assertLemma("memvonis", "me vonis");

        // rule 12 : mempe{r|l} -> mem-pe
        assertLemma("memperbaru", "me pe baru");
        assertLemma("mempelajar", "me pe ajar");

        // rule 13a : mem{rV|V} -> mem{rV|V}
        assertLemma("meminum", "me minum");

        // rule 13b : mem{rV|V} -> me-p{rV|V}
        assertLemma("memukul", "me pukul");

        // rule 14 : men{c|d|j|z} -> men-{c|d|j|z}
        assertLemma("mencinta", "me cinta");
        assertLemma("mendua", "me dua");
        assertLemma("menjauh", "me jauh");
        assertLemma("menziarah", "me ziarah");

        // rule 15a : men{V} -> me-n{V}
        assertLemma("menuklir", "me nuklir");

        // rule 15b : men{V} -> me-t{V}
        assertLemma("menangkap", "me tangkap");

        // rule 16 : meng{g|h|q} -> meng-{g|h|q}
        assertLemma("menggila", "me gila");
        assertLemma("menghajar", "me hajar");
        assertLemma("mengqasar", "me qasar");

        // rule 17a : mengV -> meng-V
        assertLemma("mengudara", "me udara");

        // rule 17b : mengV -> meng-kV
        assertLemma("mengupas", "me kupas");

        // rule 18 : menyV -> meny-sV
        assertLemma("menyuarakan", "me suara kan");

        // rule 19 : mempV -> mem-pV where V != 'e'
        assertLemma("mempopulerkan", "me populer kan");

        // rule 20 : pe{w|y}V -> pe-{w|y}V
        assertLemma("pewarna", "pe warna");
        assertLemma("peyoga", "pe yoga");

        // rule 21a : perV -> per-V
        assertLemma("peradilan", "pe adil an");

        // rule 21b : perV -> pe-rV
        assertLemma("perumahan", "pe rumah an");

        // rule 23 : perCAP -> per-CAP where C != 'r' and P != 'er'
        assertLemma("permuka", "pe muka");

        // rule 24 : perCAerV -> per-CAerV where C != 'r'
        assertLemma("perdaerah", "pe daerah");

        // rule 25 : pem{b|f|v} -> pem-{b|f|v}
        assertLemma("pembangun", "pe bangun");
        assertLemma("pemfitnah", "pe fitnah");
        assertLemma("pemvonis", "pe vonis");

        // rule 26a : pem{rV|V} -> pe-m{rV|V}
        assertLemma("peminum", "pe minum");

        // rule 26b : pem{rV|V} -> pe-p{rV|V}
        assertLemma("pemukul", "pe pukul");

        // rule 27 : men{c|d|j|z} -> men-{c|d|j|z}
        // TODO : should find more relevant examples
        assertLemma("pencinta", "pe cinta");
        assertLemma("pendua", "pe dua");
        assertLemma("penjauh", "pe jauh");
        assertLemma("penziarah", "pe ziarah");

        // rule 28a : pen{V} -> pe-n{V}
        assertLemma("penuklir", "pe nuklir");

        // rule 28b : pen{V} -> pe-t{V}
        assertLemma("penangkap", "pe tangkap");

        // rule 29 : peng{g|h|q} -> peng-{g|h|q}
        assertLemma("penggila", "pe gila");
        assertLemma("penghajar", "pe hajar");
        assertLemma("pengqasar", "pe qasar");

        // rule 30a : pengV -> peng-V
        assertLemma("pengudara", "pe udara");

        // rule 30b : pengV -> peng-kV
        assertLemma("pengupas", "pe kupas");

        // rule 31 : penyV -> peny-sV
        assertLemma("penyuara", "pe suara");

        // rule 32 : pelV -> pe-lV except pelajar -> ajar
        assertLemma("pelajar", "pe ajar");
        assertLemma("pelabuh", "pe labuh");

        // rule 33 : peCerV -> per-erV where C != {r|w|y|l|m|n}
        // TODO : find the examples
        // rule 34 : peCP -> pe-CP where C != {r|w|y|l|m|n} and P != 'er'
        assertLemma("petarung", "pe tarung");

        // rule 35 : terC1erC2 -> ter-C1erC2 where C1 != 'r'
        assertLemma("terpercaya", "te percaya");

        // rule 36 : peC1erC2 -> pe-C1erC2 where C1 != {r|w|y|l|m|n}
        assertLemma("pekerja", "pe kerja");
        assertLemma("peserta", "pe serta");

        // CS modify rule 12
        assertLemma("mempengaruhi", "me pe pengaruh i");

        // CS modify rule 16
        assertLemma("pengkritik", "pe kritik");

        // CS adjusting rule precedence
        assertLemma("bersekolah", "be sekolah");
        assertLemma("bertahan", "be tahan");
        assertLemma("mencapai", "me capai");
        assertLemma("dimulai", "di mulai");
        assertLemma("petani", "pe tani");
        assertLemma("terabai", "te abai");

        // ECS
        assertLemma("mensyaratkan", "me syarat kan");
        assertLemma("mensyukuri", "me syukur i");
        assertLemma("mengebom", "me bom");
        assertLemma("mempromosikan", "me promosi kan");
        assertLemma("memproteksi", "me proteksi");
        assertLemma("memprediksi", "me prediksi");
        assertLemma("pengkajian", "pe kaji an");
        assertLemma("pengebom", "pe bom");

        // ECS loop pengembalian akhiran
        assertLemma("bersembunyi", "be sembunyi");
        assertLemma("bersembunyilah", "be sembunyi lah");
        assertLemma("pelanggan", "pe langgan");
        assertLemma("pelaku", "pe laku");
        assertLemma("pelangganmukah", "pe langgan mu kah");
        assertLemma("pelakunyalah", "pe laku nya lah");

        assertLemma("perbaikan", "pe baik an");
        assertLemma("kebaikannya", "ke baik an nya");
        assertLemma("bisikan", "bisik an");
        assertLemma("menerangi", "me terang i");
        assertLemma("berimanlah", "be iman lah");

        assertLemma("memuaskan", "me puas kan");
        assertLemma("berpelanggan", "be pe langgan");
        assertLemma("bermakanan", "be makan an");

        // CC (Modified ECS)
        assertLemma("menyala", "me nyala");
        assertLemma("menyanyikan", "me nyanyi kan");
        assertLemma("menyatakannya", "me nyata kan nya");

        assertLemma("penyanyi", "pe nyanyi");
        assertLemma("penyawaan", "pe nyawa an");

        // CC infix
        assertLemma("rerata", "rata");
        assertLemma("lelembut", "lembut");
        assertLemma("lemigas", "ligas");
        assertLemma("kinerja", "kerja");

        // plurals
        assertLemma("buku-buku", "buku");
        assertLemma("berbalas-balasan", "be balas an");
        assertLemma("bolak-balik", "bolak-balik");

        // combination of prefix + suffix
        assertLemma("bertebaran", "be tebar an");
        assertLemma("terasingkan", "ter asing kan");
        assertLemma("membangunkan", "me bangun kan");
        assertLemma("mencintai", "me cinta i");
        assertLemma("menduakan", "me dua kan");
        assertLemma("menjauhi", "me jauh i");
        assertLemma("menggilai", "me gila i");
        assertLemma("pembangunan", "pe bangun an");

        // return the word if not found in the dictionary
        assertLemma("marwan", "marwan");
        assertLemma("subarkah", "subarkah");

        // recursively remove prefix
        assertLemma("memberdayakan", "me be daya kan");
        assertLemma("persemakmuran", "pe se makmur an");
        assertLemma("keberuntunganmu", "ke be untung an mu");
        assertLemma("kesepersepuluhnya", "ke se per sepuluh nya");

        // issues
        assertLemma("Perekonomian", "pe ekonomi an");
        assertLemma("menahan", "me tahan");

        // failed on other algorithm but we should lemmatize successfully
        assertLemma("peranan", "peran an");
        assertLemma("memberikan", "me beri kan");
        assertLemma("medannya", "medan nya");

        assertLemma("sebagai", "se bagai");
        assertLemma("bagian", "bagian an");
        assertLemma("berbadan", "be badan");
        assertLemma("abdullah", "abdullah");

        // adopted foreign suffixes
        //assertLemma("budayawan", "budaya");
        //assertLemma("karyawati", "karya");
//        assertLemma("idealis", "ideal");
//        assertLemma("idealisme", "ideal");
//        assertLemma("finalisasi", "final");

        // sastrawi additional rules
        assertLemma("mentaati", "me taat i");
        assertLemma("meniru-nirukan", "me tiru kan");
        assertLemma("menyepak-nyepak", "me sepak");

        assertLemma("melewati", "me lewat i");
        assertLemma("menganga", "me nganga");

        assertLemma("kupukul", "ku pukul");
        //assertLemma("kauhajar", "hajar");

        assertLemma("kuasa-Mu", "kuasa mu");
        assertLemma("nikmat-Ku", "nikmat ku");
        assertLemma("malaikat-malaikat-Nya", "malaikat nya");
    }
}
