package magentoegypt.locafy.manage_products_section;
import android.graphics.Bitmap;

import magentoegypt.locafy.manage_products_section.model.AttributeModelConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CombinationGenerator {
    public  List<String> generateAttributeWithValueCombinations(List<List<AttributeModelConfig>> matrix) {
        return generateAttributeWithValueCombinations(matrix, new ArrayList<String>(), 0);
    }

    private  List<String> generateAttributeWithValueCombinations(List<List<AttributeModelConfig>> matrix,
                                                                       List<String> current,
                                                                       int index) {
        if (index >= matrix.size()) {
            return new ArrayList<String>() {{
                add(String.join("::!", current));
            }};
        }

        List<String> results = new ArrayList<>();
        for (AttributeModelConfig item : matrix.get(index)) {
            List<String> newCombination = new ArrayList<>(current);
            newCombination.add((item.getAttributeCode() != null ? item.getAttributeCode() : "") +
                    ":;" +
                    (item.getValue() != null ? item.getValue() : ""));
            results.addAll(generateAttributeWithValueCombinations(matrix, newCombination, index + 1));
        }

        return results;
    }

    public List<String> generateCombinations(List<List<AttributeModelConfig>> matrix) {
        return generateCombinations(matrix, new ArrayList<String>(), 0);
    }

    private List<String> generateCombinations(List<List<AttributeModelConfig>> matrix,
                                                     List<String> current,
                                                     int index) {
        if (index >= matrix.size()) {
            return new ArrayList<String>() {{
                add(String.join("::! ", current));
            }};
        }

        List<String> results = new ArrayList<>();
        for (AttributeModelConfig item : matrix.get(index)) {
            List<String> newCombination = new ArrayList<>(current);
            newCombination.add((item.getTitle() != null ? item.getTitle() : "") +
                    ":;" +
                    (item.getLabel() != null ? item.getLabel() : ""));
            results.addAll(generateCombinations(matrix, newCombination, index + 1));
        }

        return results;
    }

    public List<String> generatePriceCombinations(List<List<AttributeModelConfig>> matrix,
                                                         AttributeModelConfig firstSelected) {
        return generatePriceCombinations(matrix, new ArrayList<String>(), 0, firstSelected)
                .stream()
                .filter(name -> !name.isEmpty())
                .collect(Collectors.toList());
    }

    private List<String> generatePriceCombinations(List<List<AttributeModelConfig>> matrix,
                                                          List<String> current,
                                                          int index,
                                                          AttributeModelConfig firstSelected) {
        if (index >= matrix.size()) {
            return current;
        }

        List<String> results = new ArrayList<>();
        for (AttributeModelConfig item : matrix.get(index)) {
            List<String> newCombination = new ArrayList<>(current);

            if (item.getPrice() != null && !item.getPrice().isEmpty() &&
                    "ApplyAttribute".equals(firstSelected.getThirdtabPriceOption()) &&
                    firstSelected.getThirdtabPriceAttributeIndex() == item.getThirdtabPriceAttributeIndex()) {
                newCombination = new ArrayList<String>() {{ add(item.getPrice()); }};
            }
            else if ("Single".equals(firstSelected.getThirdtabPriceOption())) {
                newCombination = new ArrayList<String>() {{
                    add(firstSelected.getPrice() != null ? firstSelected.getPrice() : "");
                }};
            }

            results.addAll(generatePriceCombinations(matrix, newCombination, index + 1, firstSelected));
        }

        return results;
    }

    public List<String> generateQuantityCombinations(List<List<AttributeModelConfig>> matrix,
                                                            AttributeModelConfig firstSelected) {
        return generateQuantityCombinations(matrix, new ArrayList<String>(), 0, firstSelected)
                .stream()
                .filter(name -> !name.isEmpty())
                .collect(Collectors.toList());
    }

    private List<String> generateQuantityCombinations(List<List<AttributeModelConfig>> matrix,
                                                             List<String> current,
                                                             int index,
                                                             AttributeModelConfig firstSelected) {
        if (index >= matrix.size()) {
            return current;
        }

        List<String> results = new ArrayList<>();
        for (AttributeModelConfig item : matrix.get(index)) {
            List<String> newCombination = new ArrayList<>(current);

            if (item.getQuantity() != null && !item.getQuantity().isEmpty() &&
                    "ApplyAttribute".equals(firstSelected.getThirdtabQuantityOption()) &&
                    firstSelected.getThirdtabQuantityAttributeIndex() == item.getThirdtabQuantityAttributeIndex()) {
                newCombination = new ArrayList<String>() {{ add(item.getQuantity()); }};
            }
            else if ("Single".equals(firstSelected.getThirdtabQuantityOption())) {
                newCombination = new ArrayList<String>() {{
                    add(firstSelected.getQuantity() != null ? firstSelected.getQuantity() : "");
                }};
            }

            results.addAll(generateQuantityCombinations(matrix, newCombination, index + 1, firstSelected));
        }

        return results;
    }

    public List<Bitmap> generateImageCombinations(List<List<AttributeModelConfig>> matrix,
                                                         AttributeModelConfig firstSelected) {
        return generateImageCombinations(matrix, new ArrayList<Bitmap>(), 0, firstSelected)
                .stream()
                .filter(image -> image != null)
                .collect(Collectors.toList());
    }

    private List<Bitmap> generateImageCombinations(List<List<AttributeModelConfig>> matrix,
                                                          List<Bitmap> current,
                                                          int index,
                                                          AttributeModelConfig firstSelected) {
        if (index >= matrix.size()) {
            return current;
        }

        List<Bitmap> results = new ArrayList<>();
        for (AttributeModelConfig item : matrix.get(index)) {
            List<Bitmap> newCombination = new ArrayList<>(current);

            if (item.getImage() != null &&
                    "ApplyAttribute".equals(firstSelected.getThirdtabImageOption()) &&
                    firstSelected.getThirdtabImageAttributeIndex() == item.getThirdtabImageAttributeIndex()) {
                newCombination = new ArrayList<Bitmap>() {{ add(item.getImage()); }};
            }
            else if ("Single".equals(firstSelected.getThirdtabImageOption())) {
                newCombination = new ArrayList<Bitmap>() {{
                    add(firstSelected.getImage());
                }};
            }

            results.addAll(generateImageCombinations(matrix, newCombination, index + 1, firstSelected));
        }

        return results;
    }
}
