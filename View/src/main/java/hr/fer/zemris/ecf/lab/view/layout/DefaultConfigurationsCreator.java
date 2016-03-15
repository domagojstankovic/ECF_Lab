package hr.fer.zemris.ecf.lab.view.layout;

import hr.fer.zemris.ecf.lab.engine.param.Configuration;
import hr.fer.zemris.ecf.lab.engine.param.Entry;
import hr.fer.zemris.ecf.lab.engine.param.EntryBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dstankovic on 3/1/16.
 */
public class DefaultConfigurationsCreator implements ConfigurationsCreator {
  private static class EntryPair {
    MultiEntry multiEntry;
    Entry entry; // reference matters (way to change template configuration)

    public EntryPair(MultiEntry multiEntry, Entry entry) {
      this.multiEntry = multiEntry;
      this.entry = entry;
    }
  }

  @Override
  public List<Configuration> createConfigurations(List<MultiEntryBlock> algorithmList,
                                                  List<List<MultiEntryBlock>> genotypeListBlock,
                                                  MultiEntryBlock registry) {
    List<EntryPair> multiEntryPairs = new ArrayList<>();

    Configuration configuration = createTemplateConfiguration(
        multiEntryPairs, algorithmList, genotypeListBlock, registry
    );

    return createAllConfigurations(configuration, multiEntryPairs);
  }

  private static List<Configuration> createAllConfigurations(Configuration configuration,
                                                             List<EntryPair> multiEntryPairs) {
    List<Configuration> configurations = new ArrayList<>();
    solve(configurations, configuration, multiEntryPairs, 0);
    return configurations;
  }

  private static void solve(List<Configuration> configurations,
                            Configuration configuration,
                            List<EntryPair> multiEntryPairs,
                            int index) {
    if (index >= multiEntryPairs.size()) {
      configurations.add(configuration.copy());
      return;
    }
    EntryPair pair = multiEntryPairs.get(index);
    List<String> values = pair.multiEntry.values;
    for (String value : values) {
      Entry entry = pair.entry;
      entry.value = value;
      solve(configurations, configuration, multiEntryPairs, index + 1);
    }
  }

  private static Configuration createTemplateConfiguration(List<EntryPair> multiEntryPairs,
                                                           List<MultiEntryBlock> algorithmList,
                                                           List<List<MultiEntryBlock>> genotypeListBlock,
                                                           MultiEntryBlock registry) {
    Configuration configuration = new Configuration();

    for (MultiEntryBlock algorithmBlock : algorithmList) {
      EntryBlock entryBlock = extractBlock(algorithmBlock, multiEntryPairs);
      configuration.algorithms.add(entryBlock);
    }

    for (List<MultiEntryBlock> genotypeBlock : genotypeListBlock) {
      List<EntryBlock> blocks = new ArrayList<>();
      for (MultiEntryBlock algorithmBlock : genotypeBlock) {
        EntryBlock entryBlock = extractBlock(algorithmBlock, multiEntryPairs);
        blocks.add(entryBlock);
      }
      configuration.genotypes.add(blocks);
    }

    EntryBlock entryBlock = extractBlock(registry, multiEntryPairs);
    configuration.registry = entryBlock;

    return configuration;
  }

  private static EntryBlock extractBlock(MultiEntryBlock multiEntryBlock, List<EntryPair> multiEntryPairs) {
    List<Entry> entryList = new ArrayList<>(multiEntryBlock.getEntries().size());
    EntryBlock entryBlock = new EntryBlock(multiEntryBlock.getName(), entryList);
    for (MultiEntry multiEntry : multiEntryBlock.getEntries()) {
      List<String> values = multiEntry.values;
      if (values.size() == 0) {
        continue;
      }
      Entry entry = new Entry(multiEntry.key, multiEntry.desc, multiEntry.values.get(0));
      if (values.size() > 1) {
        multiEntryPairs.add(new EntryPair(multiEntry, entry));
      }
      entryList.add(entry);
    }
    return entryBlock;
  }
}
