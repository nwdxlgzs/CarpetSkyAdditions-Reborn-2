package com.jsorrell.carpetskyadditions.config;

import com.jsorrell.carpetskyadditions.SkyAdditionsExtension;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = SkyAdditionsExtension.MOD_ID)
public class SkyAdditionsConfig implements ConfigData {
//    橡树 → Oak
//    金合欢树 → Acacia
//    云杉 → Spruce | 大云杉 → Spruce Large
//    白桦 → Birch
//    丛林树 → Jungle | 大丛林树 → Jungle Large
//    深色橡树 → Dark Oak
//    杜鹃树 → Azalea
//    红树 → Mangrove
//    樱花树 → Cherry
//    苍白橡树 → Pale Oak

    public enum InitialTreeType {
        OAK,
        ACACIA,
        SPRUCE,
        SPRUCE_LARGE,
        BIRCH,
        JUNGLE,
        JUNGLE_LARGE,
        DARK_OAK,
        AZALEA,
        MANGROVE,
        CHERRY,
        PALE_OAK,
        ;

        @Override
        public String toString() {
            switch (this) {
                case OAK -> {
                    return "Oak";
                }
                case ACACIA -> {
                    return "Acacia";
                }
                case SPRUCE -> {
                    return "Spruce";
                }
                case SPRUCE_LARGE -> {
                    return "Spruce_Large";
                }
                case BIRCH -> {
                    return "Birch";
                }
                case JUNGLE -> {
                    return "Jungle";
                }
                case JUNGLE_LARGE -> {
                    return "Jungle_Large";
                }
                case DARK_OAK -> {
                    return "Dark_Oak";
                }
                case AZALEA -> {
                    return "Azalea";
                }
                case MANGROVE -> {
                    return "Mangrove";
                }
                case CHERRY -> {
                    return "Cherry";
                }
                case PALE_OAK -> {
                    return "Pale_Oak";
                }
                default -> {
                    return null;
                }
            }
        }
    }

    public boolean defaultToSkyBlockWorld = false;
    public boolean enableDatapackByDefault = false;
    public String initialTreeType = InitialTreeType.OAK.toString();
    public boolean autoEnableDefaultSettings = true;

    private InitialTreeType parseInitialTreeType() throws ValidationException {
        switch (initialTreeType.toLowerCase()) {
            case "oak" -> {
                return InitialTreeType.OAK;
            }
            case "acacia" -> {
                return InitialTreeType.ACACIA;
            }
            case "spruce" -> {
                return InitialTreeType.SPRUCE;
            }
            case "spruce_large" -> {
                return InitialTreeType.SPRUCE_LARGE;
            }
            case "birch" -> {
                return InitialTreeType.BIRCH;
            }
            case "jungle" -> {
                return InitialTreeType.JUNGLE;
            }
            case "jungle_large" -> {
                return InitialTreeType.JUNGLE_LARGE;
            }
            case "dark_oak" -> {
                return InitialTreeType.DARK_OAK;
            }
            case "azalea" -> {
                return InitialTreeType.AZALEA;
            }
            case "mangrove" -> {
                return InitialTreeType.MANGROVE;
            }
            case "cherry" -> {
                return InitialTreeType.CHERRY;
            }
            case "pale_oak" -> {
                return InitialTreeType.PALE_OAK;
            }
            default -> throw new ValidationException("Couldn't parse initialTreeType: " + initialTreeType);
        }
    }

    public InitialTreeType getInitialTreeType() {
        try {
            return parseInitialTreeType();
        } catch (ValidationException e) {
            throw new AssertionError("Invalid tree type");
        }
    }

    @Override
    public void validatePostLoad() throws ValidationException {
        parseInitialTreeType();
    }
}
