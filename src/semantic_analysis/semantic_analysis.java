package semantic_analysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class semantic_analysis {
	public ArrayList<String> cleaned_news = new ArrayList<>();
	public ArrayList<String> article_list = new ArrayList<>();
	public ArrayList<Double> total_words = new ArrayList<>();
	public ArrayList<Double> frequency = new ArrayList<>();
	public ArrayList<Double> relative_frequency = new ArrayList<>();
	double fraction_canada;
	double log_canada;
	double fraction_halifax;
	double log_halifax;
	double fraction_novascotia;
	double log_novascotia;

	public void read_all_files() throws FileNotFoundException, IOException {
		for (int i = 0; i < 1578; i++) {
			try (BufferedReader br = new BufferedReader(new FileReader("src/text_files/output" + i + ".txt"))) {
				String line;
				String final_string = "";
				while ((line = br.readLine()) != null) {
					String cleaned_news_line = clean_news(line);
					final_string = final_string + " " + cleaned_news_line;

				}
				cleaned_news.add(final_string);
			}

		}

	}

	public String clean_news(String raw_line) {
		String cleaned_text = raw_line;
		String[][] replacements = { { "<TITLE>", "TITLE:" }, { "</TITLE>", "" }, { "<AUTHOR>", "AUTHOR:" },
				{ "</AUTHOR>", "" }, { "<DATELINE>", "DATELINE:" }, { "</DATELINE>", "" }, { "<BODY>", "TEXTBODY:" },
				{ "</BODY>", "" }, { "<Reuter>", "" }, { "</Reuter>", "" }, { "   ", " " } };
		for (String[] replacement : replacements) {
			cleaned_text = cleaned_text.replace(replacement[0], replacement[1]);
		}
		cleaned_text = cleaned_text.replaceAll("[^a-zA-Z0-9\\s]+", " ");
		return cleaned_text;
	}

	public void compute_TF_IDF() {
		int count_Canada = 0;
		int count_Halifax = 0;
		int count_novascotia = 0;
		int canada_match = 0;
		int count = 0;
		for (int i = 0; i < 1578; i++) {

			if (cleaned_news.get(i).toLowerCase().contains("canada")) {
				++count_Canada;
			}
			else if (cleaned_news.get(i).toLowerCase().contains("Halifax")) {
				++count_Halifax;
			}
			else if (cleaned_news.get(i).toLowerCase().contains("Nova Sotia")) {
				++count_novascotia;
			}

		}

		double total = 1578;
		double fraction_canada = total / count_Canada;
		double log_canada = Math.log10(fraction_canada);
		double fraction_halifax = total / count_Halifax;
		double log_halifax = Math.log10(fraction_halifax);
		double fraction_novascotia = total / count_novascotia;
		double log_novascotia = Math.log10(fraction_novascotia);
		System.out.println("Log10 for Canada:" + log_canada);
		System.out.println("Log10 for Halifax:" + log_halifax);
		System.out.println("Log10 for Nova Scotia:" + log_novascotia);
	}

	public void count_canada() {
		for (int i = 0; i < 1578; i++) {
			int f = 0;
			int word_count = 0;
			String[] texts = cleaned_news.get(i).split(" ");
			for (int j = 0; j < texts.length; j++) {
				if (texts[j].length() > 0) {
					word_count++;
					String ij = texts[j].toLowerCase();
					if (ij.equals("canada")) {
						f++;
					}
				}
			}
			article_list.add("Article #" + i);
			total_words.add((double) word_count);
			frequency.add((double) f);
		}
		for (int i = 0; i < article_list.size(); i++) {
			relative_frequency.add(frequency.get(i) / total_words.get(i));
		}
		double max_rel_freq = Collections.max(relative_frequency);
		
		// https://stackoverflow.com/questions/10437623/how-to-find-the-max-value-of-an-arraylist-and-two-of-its-index-position-using-ja
		int index = relative_frequency.indexOf(max_rel_freq);
		System.out.println("Article which has the highest relative frequency:" + "Article #" + index);

	}

	public static void main(String[] args) throws FileNotFoundException, IOException {

		semantic_analysis sma = new semantic_analysis();
		sma.read_all_files();
		sma.compute_TF_IDF();
		sma.count_canada();

	}
}
