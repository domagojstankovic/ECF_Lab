package hr.fer.zemris.ecf.lab.view.layout;

import hr.fer.zemris.ecf.lab.engine.param.Configuration;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dstankovic on 3/2/16.
 */
public class DefaultConfigurationsCreatorTest {

  @Test
  public void testCreateConfigurations() throws Exception {
    DefaultConfigurationsCreator creator = new DefaultConfigurationsCreator();

    List<MultiEntryBlock> algorithmList = new ArrayList<>(1);
    algorithmList.add(createMultiEntryBlock("alg-A", 1, 1));

    List<List<MultiEntryBlock>> genotypeListBlock = new ArrayList<>(1);
    List<MultiEntryBlock> genotypeBlocks = new ArrayList<>(2);
    genotypeListBlock.add(genotypeBlocks);
    genotypeBlocks.add(createMultiEntryBlock("gen-A", 1, 2, 2));
    genotypeBlocks.add(createMultiEntryBlock("gen-B", 1));


    MultiEntryBlock registry = createMultiEntryBlock("reg", 1, 1, 3, 1);

    List<Configuration> configurations = creator.createConfigurations(algorithmList, genotypeListBlock, registry);
    Assert.assertTrue(configurations.size() == 12);

    for (Configuration conf : configurations) {
      Assert.assertTrue(conf.algorithms.size() == 1);
      Assert.assertTrue(conf.algorithms.get(0).getEntryList().size() == 2);
      Assert.assertTrue(conf.algorithms.get(0).getEntryAt(0).value.equals("alg-A-1 1"));
      Assert.assertTrue(conf.algorithms.get(0).getEntryAt(1).value.equals("alg-A-2 1"));
    }
  }

  private static MultiEntryBlock createMultiEntryBlock(String name, int... counts) {
    List<MultiEntry> regEntries = new ArrayList<>(counts.length);
    for (int i = 0; i < counts.length; i++) {
      regEntries.add(new MultiEntry(name, "", createValues(name + "-" + (i+1), counts[i])));
    }
    return new MultiEntryBlock(name, regEntries);
  }

  private static List<String> createValues(String prefix, int count) {
    List<String> values = new ArrayList<>(count);
    for (int i = 1; i <= count; i++) {
      values.add(prefix + " " + i);
    }
    return values;
  }
}
