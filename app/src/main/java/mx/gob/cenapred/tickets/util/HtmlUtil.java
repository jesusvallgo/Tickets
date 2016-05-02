package mx.gob.cenapred.tickets.util;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BulletSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtil {
    public CharSequence parseSimpleUL(String cadena){
        List<String> itemsList = this.itemList(cadena);
        CharSequence itemsBullet = this.itemBullet(itemsList);
        CharSequence contenido = cadena.replaceAll("<ul>.*?</ul>", "\n");
        return TextUtils.concat(contenido, itemsBullet);
    }

    // Metodo para obtener cada elemento de la lista <li></li>
    private List<String> itemList(String str) {
        final Pattern REGEXP_LIST = Pattern.compile("<li>(.+?)</li>");

        final List<String> items = new ArrayList<>();
        final Matcher matcher = REGEXP_LIST.matcher(str);
        while (matcher.find()) {
            items.add(matcher.group(1));
        }
        return items;
    }

    // Metodo para generar la lista de bullets a partir de un array
    public CharSequence itemBullet(List<String> itemsLista){
        SpannableStringBuilder spanBuilder = new SpannableStringBuilder();
        Integer contItemsLista = 0;
        for (String item: itemsLista) {
            CharSequence line = item + (contItemsLista < itemsLista.size()-1 ? "\n" : "");
            Spannable span = new SpannableString(line);
            span.setSpan(new BulletSpan(12),0,span.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spanBuilder.append(span);
        }

        return spanBuilder;
    }
}