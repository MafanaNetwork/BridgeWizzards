package me.TahaCheji.util;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class CompactInventory implements PersistentDataType<byte[], ItemStack[]> {
    private Charset charset = Charset.defaultCharset();
    private static final byte PADDING = 127;

    public CompactInventory() {
    }

    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    public Class<ItemStack[]> getComplexType() {
        return ItemStack[].class;
    }

    public byte[] toPrimitive(ItemStack[] items, PersistentDataAdapterContext itemTagAdapterContext) {
        return this.itemsToString(items).getBytes(this.charset);
    }

    public ItemStack[] fromPrimitive(byte[] bytes, PersistentDataAdapterContext itemTagAdapterContext) {
        ItemStack[] items = this.stringToItems(new String(bytes, this.charset));
        return items;
    }

    private String itemsToString(ItemStack[] items) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this.serializeItemStack(items));
            oos.flush();
            return printBase64Binary(bos.toByteArray());
        } catch (Exception var4) {
            return "";
        }
    }

    private ItemStack[] stringToItems(String s) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(parseBase64Binary(s));
            ObjectInputStream ois = new ObjectInputStream(bis);
            return this.deserializeItemStack((Map[])((Map[])ois.readObject()));
        } catch (Exception var4) {
            return new ItemStack[]{new ItemStack(Material.AIR)};
        }
    }

    private Map<String, Object>[] serializeItemStack(ItemStack[] items) {
        Map<String, Object>[] result = new Map[items.length];

        for(int i = 0; i < items.length; ++i) {
            ItemStack is = items[i];
            if (is == null) {
                result[i] = new HashMap();
            } else {
                result[i] = is.serialize();
                if (is.hasItemMeta()) {
                    result[i].put("meta", is.getItemMeta().serialize());
                }
            }
        }

        return result;
    }

    private ItemStack[] deserializeItemStack(Map<String, Object>[] map) {
        ItemStack[] items = new ItemStack[map.length];

        for(int i = 0; i < items.length; ++i) {
            Map<String, Object> s = map[i];
            if (s.size() == 0) {
                items[i] = null;
            } else {
                try {
                    if (s.containsKey("meta")) {
                        Map<String, Object> im = new HashMap((Map)s.remove("meta"));
                        im.put("==", "ItemMeta");
                        ItemStack is = ItemStack.deserialize(s);
                        is.setItemMeta((ItemMeta) ConfigurationSerialization.deserializeObject(im));
                        items[i] = is;
                    } else {
                        items[i] = ItemStack.deserialize(s);
                    }
                } catch (Exception var7) {
                    items[i] = null;
                }
            }
        }

        return items;
    }

    private static int guessLength(String text) {
        int len = text.length();

        int j;
        for(j = len - 1; j >= 0; --j) {
            byte code = decodeMap()[text.charAt(j)];
            if (code != 127) {
                if (code == -1) {
                    return text.length() / 4 * 3;
                }
                break;
            }
        }

        ++j;
        int padSize = len - j;
        return padSize > 2 ? text.length() / 4 * 3 : text.length() / 4 * 3 - padSize;
    }

    private static byte[] decodeMap() {
        byte[] map = new byte[128];

        int i;
        for(i = 0; i < 128; ++i) {
            map[i] = -1;
        }

        for(i = 65; i <= 90; ++i) {
            map[i] = (byte)(i - 65);
        }

        for(i = 97; i <= 122; ++i) {
            map[i] = (byte)(i - 97 + 26);
        }

        for(i = 48; i <= 57; ++i) {
            map[i] = (byte)(i - 48 + 52);
        }

        map[43] = 62;
        map[47] = 63;
        map[61] = 127;
        return map;
    }

    private static byte[] parseBase64Binary(String text) {
        int buflen = guessLength(text);
        byte[] out = new byte[buflen];
        int o = 0;
        int len = text.length();
        byte[] quadruplet = new byte[4];
        int q = 0;

        for(int i = 0; i < len; ++i) {
            char ch = text.charAt(i);
            byte v = decodeMap()[ch];
            if (v != -1) {
                quadruplet[q++] = v;
            }

            if (q == 4) {
                out[o++] = (byte)(quadruplet[0] << 2 | quadruplet[1] >> 4);
                if (quadruplet[2] != 127) {
                    out[o++] = (byte)(quadruplet[1] << 4 | quadruplet[2] >> 2);
                }

                if (quadruplet[3] != 127) {
                    out[o++] = (byte)(quadruplet[2] << 6 | quadruplet[3]);
                }

                q = 0;
            }
        }

        return out;
    }

    private static String printBase64Binary(byte[] input) {
        return printBase64Binary(input, 0, input.length);
    }

    private static String printBase64Binary(byte[] input, int offset, int len) {
        char[] buf = new char[(len + 2) / 3 * 4];
        int ptr = printBase64Binary(input, offset, len, buf, 0);

        assert ptr == buf.length;

        return new String(buf);
    }

    private static int printBase64Binary(byte[] input, int offset, int len, char[] buf, int ptr) {
        int remaining = len;

        int i;
        for(i = offset; remaining >= 3; i += 3) {
            buf[ptr++] = encode(input[i] >> 2);
            buf[ptr++] = encode((input[i] & 3) << 4 | input[i + 1] >> 4 & 15);
            buf[ptr++] = encode((input[i + 1] & 15) << 2 | input[i + 2] >> 6 & 3);
            buf[ptr++] = encode(input[i + 2] & 63);
            remaining -= 3;
        }

        if (remaining == 1) {
            buf[ptr++] = encode(input[i] >> 2);
            buf[ptr++] = encode((input[i] & 3) << 4);
            buf[ptr++] = '=';
            buf[ptr++] = '=';
        }

        if (remaining == 2) {
            buf[ptr++] = encode(input[i] >> 2);
            buf[ptr++] = encode((input[i] & 3) << 4 | input[i + 1] >> 4 & 15);
            buf[ptr++] = encode((input[i + 1] & 15) << 2);
            buf[ptr++] = '=';
        }

        return ptr;
    }

    private static char encode(int i) {
        return encodeMap()[i & 63];
    }

    private static char[] encodeMap() {
        char[] map = new char[64];

        int i;
        for(i = 0; i < 26; ++i) {
            map[i] = (char)(65 + i);
        }

        for(i = 26; i < 52; ++i) {
            map[i] = (char)(97 + (i - 26));
        }

        for(i = 52; i < 62; ++i) {
            map[i] = (char)(48 + (i - 52));
        }

        map[62] = '+';
        map[63] = '/';
        return map;
    }
}
